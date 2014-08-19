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
		String servletPath = httpReq.getServletPath();// index.jsp or /rest
		String pathInfo = httpReq.getPathInfo();// null or /runtime/dbpool

		if (isCallToRuntimeRestInterface(servletPath, pathInfo)) {
			chain.doFilter(request, response);
		} else if (isCallWithoutCommunitySubdomain(domain)) {
			if (isCallToPlatformPage(servletPath)) {
				httpResp.sendRedirect("portal.jsp");
			} else {
				chain.doFilter(request, response);
			}
		} else if (isCallToPortalPage(servletPath)) {
			httpResp.sendRedirect("./");
		} else {
			CommunitiesRecord community = getCommunity(domain);
			if (community != null) {
				Community.set(httpReq, community);
				chain.doFilter(request, response);
			} else {
				httpResp.sendRedirect(httpReq.getProtocol() + "://" + removeSubDomains(domain)
						+ (request.getServerPort() != 80 ? ":" + request.getServerPort() : ""));
			}
		}

	}

	String removeSubDomains(String domain) {
		String[] arr = domain.split("\\.");
		if (arr.length == 1) {
			return arr[0];
		}
		return arr[arr.length - 2] + "." + arr[arr.length - 1];
	}

	private CommunitiesRecord getCommunity(String domain) {
		String subdomain = domain.indexOf('.') > -1 ? domain.substring(0, domain.indexOf('.')) : domain;
		CommunitiesRecord community = CommunityDao.INSTANCE.getByDomain(subdomain);
		return community;
	}

	private boolean isCallToPortalPage(String servletPath) {
		return "/portal.jsp".equals(servletPath);
	}

	private boolean isCallToPlatformPage(String servletPath) {
		return "/index.jsp".equals(servletPath);
	}

	private boolean isCallWithoutCommunitySubdomain(String domain) {
		return CharMatcher.is('.').countIn(domain) == 1 || domain.startsWith("www.");
	}

	private boolean isCallToRuntimeRestInterface(String servletPath, String pathInfo) {
		return "/rest".equals(servletPath) && pathInfo.startsWith("/runtime");
	}
}
