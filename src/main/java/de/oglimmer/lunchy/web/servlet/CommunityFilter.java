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

import de.oglimmer.lunchy.database.CommunityDao;
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
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpServletResponse httpResp = (HttpServletResponse) response;

		String domain = request.getServerName();
		String path = httpReq.getServletPath();

		if (isCallToRuntimeRestInterface(path)) {
			chain.doFilter(request, response);
		} else if (isCallWithoutCommunitySubdomain(domain)) {
			if (isCallToPlatformPage(path)) {
				httpResp.sendRedirect("portal.jsp");
			} else {
				chain.doFilter(request, response);
			}
		} else if (isCallToPortalPage(path)) {
			httpResp.sendRedirect("index.jsp");
		} else {
			CommunitiesRecord community = getCommunity(domain);
			if (community != null) {
				Community.set(httpReq, community);
				chain.doFilter(request, response);
			} else {
				httpResp.sendRedirect("http://lunchylunch.com" + (request.getServerPort() != 80 ? ":" + request.getServerPort() : "")
						+ "/lunchy/portal.jsp");
			}
		}

	}

	private boolean isCallToPortalPage(String path) {
		return "/portal.jsp".equals(path);
	}

	private CommunitiesRecord getCommunity(String domain) {
		String subdomain = domain.indexOf('.') > -1 ? domain.substring(0, domain.indexOf('.')) : domain;
		CommunitiesRecord community = CommunityDao.INSTANCE.getByDomain(subdomain);
		return community;
	}

	private boolean isCallToPlatformPage(String path) {
		return "/".equals(path) || "/index.jsp".equals(path);
	}

	private boolean isCallWithoutCommunitySubdomain(String domain) {
		return "lunchylunch.com".equalsIgnoreCase(domain) || "www.lunchylunch.com".equalsIgnoreCase(domain);
	}

	private boolean isCallToRuntimeRestInterface(String path) {
		return path.startsWith("/runtime");
	}
}
