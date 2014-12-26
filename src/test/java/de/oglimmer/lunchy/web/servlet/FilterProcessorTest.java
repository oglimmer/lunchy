package de.oglimmer.lunchy.web.servlet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.google.common.cache.CacheLoader.InvalidCacheLoadException;

import de.oglimmer.lunchy.database.dao.CommunityDao;
import de.oglimmer.lunchy.database.generated.tables.records.CommunitiesRecord;
import de.oglimmer.lunchy.web.servlet.CommunityFilter.FilterProcessor;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ CommunityDao.class })
public class FilterProcessorTest {

	private HttpServletRequest request;
	private HttpServletResponse response;

	@Before
	public void init() {
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
	}

	/**
	 * http://www.lunchylunch.com/lunchy/rest/runtime/dbpool
	 * 
	 * @throws Exception
	 */
	@Test
	public void testRuntime() throws Exception {

		String[] serverNames = { "www.lunchylunch.com", "lunchylunch.com", "demo.lunchylunch.com", "localhost" };
		String[] interfaceEndpoints = { "dbpool", "rest", "notifications" };

		for (String serverName : serverNames) {
			for (String interfaceEndpoint : interfaceEndpoints) {
				stubTestRuntime(serverName, interfaceEndpoint);

				FilterChain chain = mock(FilterChain.class);
				run(chain);

				verify(chain).doFilter(request, response);
				verify(request, never()).setAttribute(anyString(), anyObject());
				verify(response, never()).sendRedirect(anyString());
			}
		}
	}

	/**
	 * http://www.lunchylunch.com/lunchy/index.jsp
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCallToIndexOnInfoPage() throws Exception {
		String[] serverNames = { "www.lunchylunch.com", "lunchylunch.com" };
		for (String serverName : serverNames) {
			init();
			stubCallToIndexOnInfoPage(serverName);

			FilterChain chain = mock(FilterChain.class);
			run(chain);

			verifyZeroInteractions(chain);
			verify(response).sendRedirect("portal.jsp");
			verify(request, never()).setAttribute(anyString(), anyObject());
		}
	}

	/**
	 * http://www.lunchylunch.com/lunchy/portal.jsp
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCallToPortalOnInfoPage() throws Exception {
		String[] serverNames = { "www.lunchylunch.com", "lunchylunch.com" };
		for (String serverName : serverNames) {
			stubCallToPortal(serverName);

			FilterChain chain = mock(FilterChain.class);
			run(chain);

			verify(chain).doFilter(request, response);
			verify(request, never()).setAttribute(anyString(), anyObject());
			verify(response, never()).sendRedirect(anyString());
		}
	}

	/**
	 * http://demo.lunchylunch.com/lunchy/portal.jsp
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCallToPortalOnCommPage() throws Exception {
		String[] serverNames = { "demo.lunchylunch.com", "localhost" };
		for (String serverName : serverNames) {
			init();
			stubCallToPortal(serverName);

			FilterChain chain = mock(FilterChain.class);
			run(chain);

			verifyZeroInteractions(chain);
			verify(response).sendRedirect("./");
			verify(request, never()).setAttribute(anyString(), anyObject());
		}
	}

	/**
	 * http://demo.lunchylunch.com/lunchy/index.jsp
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCallToIndexOnCommPage() throws Exception {
		String[] serverNames = { "demo.lunchylunch.com", "localhost" };
		for (String serverName : serverNames) {

			// result-objects and stubs
			CommunitiesRecord rec = new CommunitiesRecord();
			final Object[] requestAttributes = new Object[2];
			stubCallToIndexOnCommPage(serverName, requestAttributes, rec);

			// test
			FilterChain chain = mock(FilterChain.class);
			run(chain);

			// verify
			verify(chain).doFilter(request, response);
			verify(response, never()).sendRedirect(anyString());
			assertThat((String) requestAttributes[0], equalTo("community"));
			assertThat((CommunitiesRecord) requestAttributes[1], sameInstance(rec));
		}
	}

	/**
	 * http://foo.lunchylunch.com/lunchy/index.jsp
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCallToIndexOnNonExistingCommPage() throws Exception {
		stubCallToIndexOnNonExistingCommPage();

		FilterChain chain = mock(FilterChain.class);
		run(chain);

		verifyZeroInteractions(chain);
		verify(request, never()).setAttribute(anyString(), anyObject());
		verify(response).sendRedirect("https://lunchylunch.com:8080");
	}

	private void stubTestRuntime(String serverName, String interfaceEndpoint) throws IOException, ServletException {
		when(request.getServerName()).thenReturn(serverName);
		when(request.getServletPath()).thenReturn("/rest");
		when(request.getPathInfo()).thenReturn("/runtime/" + interfaceEndpoint);
	}

	private void stubCallToIndexOnInfoPage(String serverName) throws IOException, ServletException {
		when(request.getServerName()).thenReturn(serverName);
		when(request.getServletPath()).thenReturn("/index.jsp");
	}

	private void stubCallToPortal(String serverName) throws IOException, ServletException {
		when(request.getServerName()).thenReturn(serverName);
		when(request.getServletPath()).thenReturn("/portal.jsp");

	}

	private void stubCallToIndexOnCommPage(String serverName, final Object[] requestAttributes, CommunitiesRecord rec)
			throws IOException, ServletException {
		CommunityDao mockInstance = PowerMockito.mock(CommunityDao.class);

		Whitebox.setInternalState(CommunityDao.class, "INSTANCE", mockInstance);
		PowerMockito.when(mockInstance.getByDomain(anyString())).thenReturn(rec);
		when(request.getServerName()).thenReturn(serverName);
		when(request.getServletPath()).thenReturn("/index.jsp");
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				requestAttributes[0] = invocation.getArguments()[0];
				requestAttributes[1] = invocation.getArguments()[1];
				return null;
			}
		}).when(request).setAttribute(anyString(), anyObject());

	}

	private void stubCallToIndexOnNonExistingCommPage() {
		// create mocks and helper-objects
		CommunityDao mockInstance = PowerMockito.mock(CommunityDao.class);

		// stub methods
		Whitebox.setInternalState(CommunityDao.class, "INSTANCE", mockInstance);
		PowerMockito.when(mockInstance.getByDomain(anyString())).thenThrow(
				new InvalidCacheLoadException("CacheLoader returned null for key foo."));
		when(request.getServerName()).thenReturn("foo.lunchylunch.com");
		when(request.getServletPath()).thenReturn("/index.jsp");
		when(request.getScheme()).thenReturn("https");
		when(request.getServerPort()).thenReturn(8080);
	}

	private void run(FilterChain chain) throws IOException, ServletException {
		CommunityFilter.FilterProcessor fp = new FilterProcessor(request, response);
		fp.doFilter(chain);
	}
}
