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

import de.oglimmer.lunchy.services.BotDetectionService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebFilter(urlPatterns = { "/", "/index.jsp", "/portal.jsp" })
public class BotFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpReq = (HttpServletRequest) request;

		String userAgent = httpReq.getHeader("User-Agent");
		log.trace("UA: " + userAgent);
		
		if (isBot(userAgent)) {
			log.info("Bot found: " + userAgent);
			RequestDispatcher view = httpReq.getRequestDispatcher("/static_portal.html");
			view.forward(request, response);
		} else {
			chain.doFilter(request, response);
		}
	}

	private boolean isBot(String userAgent) {
		return BotDetectionService.INSTANCE.isBot(userAgent);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

}