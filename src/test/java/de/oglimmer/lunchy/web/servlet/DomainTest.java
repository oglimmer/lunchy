package de.oglimmer.lunchy.web.servlet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

public class DomainTest {

	@Test
	public void testRemoveSubDomainSingle() {
		CommunityFilter filter = new CommunityFilter();
		String filteredDomain = filter.removeSubDomains("localhost");
		assertThat(filteredDomain, equalTo("localhost"));
	}

	@Test
	public void testRemoveSubDomainDomain() {
		CommunityFilter filter = new CommunityFilter();
		String filteredDomain = filter.removeSubDomains("nicedomain.com");
		assertThat(filteredDomain, equalTo("nicedomain.com"));
	}

	@Test
	public void testRemoveSubDomainSubdomain() {
		CommunityFilter filter = new CommunityFilter();
		String filteredDomain = filter.removeSubDomains("www.nicedomain.com");
		assertThat(filteredDomain, equalTo("nicedomain.com"));
	}

	@Test
	public void testRemoveSubDomainTwoSubdomains() {
		CommunityFilter filter = new CommunityFilter();
		String filteredDomain = filter.removeSubDomains("more.some.nicedomain.com");
		assertThat(filteredDomain, equalTo("nicedomain.com"));
	}
}
