package de.oglimmer.lunchy.web.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebFilter(urlPatterns = { "/", "/index.jsp", "/portal.jsp" })
public class BotFilter implements Filter {

	// as per https://support.google.com/webmasters/answer/1061943?hl=en
	private static final String GOOGLEBOT_1 = "Googlebot/2.1 (+http://www.google.com/bot.html)";
	private static final String GOOGLEBOT_2 = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpReq = (HttpServletRequest) request;

		String userAgent = httpReq.getHeader("User-Agent");

		if (isBot(userAgent)) {
			log.info("Bot found: " + userAgent);
			RequestDispatcher view = httpReq.getRequestDispatcher("/static_portal.html");
			view.forward(request, response);
		} else {
			chain.doFilter(request, response);
		}
	}

	private boolean isBot(String userAgent) {
		if (userAgent == null || userAgent.isEmpty()) {
			return false;
		}
		String lowerCaseUserAgent = userAgent.toLowerCase().trim();

		// As google can understand Angular, we don't treat it as a bot
		if (GOOGLEBOT_2.equalsIgnoreCase(lowerCaseUserAgent) || GOOGLEBOT_1.equalsIgnoreCase(lowerCaseUserAgent)) {
			return false;
		}

		return lowerCaseUserAgent.contains("bot") || lowerCaseUserAgent.contains("spider")
				|| lowerCaseUserAgent.contains("crawler");
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

}