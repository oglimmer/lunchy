package de.oglimmer.lunchy.database.dao;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import de.oglimmer.lunchy.database.generated.tables.records.UsageStatisticsRecord;
import de.oglimmer.lunchy.rest.SessionProvider;
import de.oglimmer.lunchy.rest.dto.LoginResponse;
import de.oglimmer.lunchy.rest.resources.helper.LoginCheck;
import de.oglimmer.lunchy.services.CommunityService;
import de.oglimmer.lunchy.services.CookieService;
import de.oglimmer.lunchy.services.DateCalcService;
import de.oglimmer.lunchy.services.LunchyProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static de.oglimmer.lunchy.database.dao.DaoBackend.DB;

@Slf4j
public enum UsageDao {
	INSTANCE;

	private LoadingCache<String, CountryCity> countryCityCache;

	UsageDao() {
		countryCityCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.DAYS)
				.build(new CacheLoader<String, CountryCity>() {
					@Override
					public CountryCity load(String ip) throws Exception {
						return getGeoData(ip);
					}
				});
	}

	private ExecutorService execServ = Executors.newSingleThreadExecutor();

	public void store(String action, String context, HttpServletRequest request, LongTimeToken longTimeToken) {
		Timestamp timestamp = DateCalcService.getNow();
		String ip = getRemoteIP(request);
		String userAgent = request.getHeader("User-Agent");
		Cookie ltsCookie = CookieService.INSTANCE.getLongTermSessionCookie(request);
		String userCookie = ltsCookie != null ? ltsCookie.getValue() : null;
		int domain = getDomain(request);
		Integer userId = getUserId(request, longTimeToken);
		String referer = request.getHeader("referer");
		execServ.submit(() -> {
			UsageStatisticsRecord usage = new UsageStatisticsRecord();
			usage.setAction(action);
			usage.setContext(context);
			usage.setIp(ip);
			usage.setUserAgent(userAgent);
			usage.setCreatedOn(timestamp);
			usage.setUserCookie(userCookie);
			usage.setDomain(domain);
			usage.setUserId(userId);
			usage.setReferer(referer);
			try {
				CountryCity cc = countryCityCache.get(ip);
				usage.setCountry(cc.getCountry());
				usage.setCity(cc.getCity());
			} catch (Exception e) {
				log.error("Failed to get CountryCity from cache", e);
			}
			DB.store(usage);
		});
	}

	private CountryCity getGeoData(String ip) throws IOException {
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			final String uri = String.format("http://api.ipstack.com/%s?access_key=%s", ip,
					LunchyProperties.INSTANCE.getIpStackApiKey());
			try (CloseableHttpResponse response = httpclient.execute(new HttpGet(uri))) {
				String resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
				try (JsonReader jsonReader = Json.createReader(new StringReader(resultString))) {
					try {
						JsonObject jsonResponse = jsonReader.readObject();
						String country = jsonResponse.containsValue("country_name") ? jsonResponse.getString("country_name") : null;
						String city = jsonResponse.containsValue("city") ? jsonResponse.getString("city") : null;
						return new CountryCity(country, city);
					} catch (javax.json.stream.JsonParsingException e) {
						log.error("Failed to parse json from api.ipstack.com. JSON: {}", resultString);
						throw e;
					}
				}
			}
		}
	}

	private int getDomain(HttpServletRequest request) {
		if (request.getAttribute("community") != null) {
			return CommunityService.get(request);
		} else {
			return -1;
		}
	}

	/**
	 * Logic: Always use HTTP_X_FORWARDED_FOR if provided. Otherwise use the IP from the request if this is not
	 * 127.0.0.1 (=behind a local reverse proxy) then use X-Forwarded-For (default for haproxy)
	 * 
	 * @param request
	 * @return
	 */
	private String getRemoteIP(HttpServletRequest request) {
		final String remoteAddr;

		for(Enumeration<String> headerNames = request.getHeaderNames() ; headerNames.hasMoreElements(); ) {
			String headerName = headerNames.nextElement();
			System.out.println(request.getHeader(headerName));
		}

		String httpXForwardedFor = request.getHeader("HTTP_X_FORWARDED_FOR");
		if (httpXForwardedFor != null) {
			remoteAddr = httpXForwardedFor;
		} else {
			String xForwardedFor = request.getHeader("X-Forwarded-For");
			if (xForwardedFor != null) {
				remoteAddr = xForwardedFor;
			} else {
				remoteAddr = request.getRemoteAddr();
			}
		}
		return remoteAddr;
	}

	private Integer getUserId(HttpServletRequest request, LongTimeToken longTimeToken) {
		Integer userId = SessionProvider.INSTANCE.getLoggedInUserId(request);
		if (userId == null && longTimeToken != null && longTimeToken.getLongTimeToken() != null) {
			LoginResponse resp = new LoginCheck(request, longTimeToken.getLongTimeToken()).check();
			userId = resp.getUserId();
		}
		return userId;
	}

	@ToString
	public static class LongTimeToken {

		@Getter
		@Setter
		private String longTimeToken;

	}

	@Value
	class CountryCity {
		private String country;
		private String city;
	}

}
