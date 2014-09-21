package de.oglimmer.lunchy.web.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.CharMatcher;

import de.oglimmer.lunchy.database.dao.CommunityDao;
import de.oglimmer.lunchy.database.generated.tables.records.CommunitiesRecord;
import de.oglimmer.lunchy.services.Community;

@WebFilter(urlPatterns = "/*")
public class CommunityFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpServletResponse httpResp = (HttpServletResponse) response;

		FilterProcessor fp = new FilterProcessor(httpReq, httpResp);
		fp.doFilter(chain);
	}

	class FilterProcessor {

		private HttpServletRequest request;
		private HttpServletResponse response;

		private String domain;
		private String servletPath;
		private String pathInfo;

		public FilterProcessor(HttpServletRequest httpReq, HttpServletResponse httpResp) {
			this.request = httpReq;
			this.response = httpResp;
			domain = httpReq.getServerName();
			servletPath = httpReq.getServletPath();// index.jsp or /rest
			pathInfo = httpReq.getPathInfo();// null or /runtime/dbpool
		}

		public void doFilter(FilterChain chain) throws IOException, ServletException {
			if (isCallToRuntimeRestInterface()) {
				processCallToRuntime(chain);
			} else if (isCallWithoutCommunitySubdomain()) {
				processCallWithoutCommunitySubdomain(chain);
			} else if (isCallToPortalPage()) {
				processCallToPortalPage();
			} else {
				processCallToCommunityDomain(chain);
			}
		}

		private void processCallToCommunityDomain(FilterChain chain) throws IOException, ServletException {
			CommunitiesRecord community = getCommunity();
			if (community != null) {
				Community.set(request, community);
				processCallToRuntime(chain);
			} else {
				String redirect = request.getScheme() + "://" + removeSubDomains(domain)
						+ (request.getServerPort() != 80 ? ":" + request.getServerPort() : "");
				response.sendRedirect(redirect);
			}
		}

		private void processCallToPortalPage() throws IOException {
			response.sendRedirect("./");
		}

		private void processCallWithoutCommunitySubdomain(FilterChain chain) throws IOException, ServletException {
			if (isCallToPlatformPage()) {
				response.sendRedirect("portal.jsp");
			} else {
				processCallToRuntime(chain);
			}
		}

		private void processCallToRuntime(FilterChain chain) throws IOException, ServletException {
			chain.doFilter(request, response);
		}

		private CommunitiesRecord getCommunity() {
			String subdomain = domain.indexOf('.') > -1 ? domain.substring(0, domain.indexOf('.')) : domain;
			CommunitiesRecord community = CommunityDao.INSTANCE.getByDomain(subdomain);
			return community;
		}

		private boolean isCallToPortalPage() {
			return "/portal.jsp".equals(servletPath);
		}

		private boolean isCallToPlatformPage() {
			return "/index.jsp".equals(servletPath) || "/".equals(servletPath);
		}

		private boolean isCallWithoutCommunitySubdomain() {
			return CharMatcher.is('.').countIn(domain) == 1 || domain.startsWith("www.");
		}

		private boolean isCallToRuntimeRestInterface() {
			return "/rest".equals(servletPath) && pathInfo.startsWith("/runtime");
		}
	}

	static String removeSubDomains(String domain) {
		String[] arr = domain.split("\\.");
		if (arr.length == 1) {
			return arr[0];
		}
		return arr[arr.length - 2] + "." + arr[arr.length - 1];
	}

}
