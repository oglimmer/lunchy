package de.oglimmer.lunchy.web.servlet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

public class DomainTest {

	@Test
	public void testRemoveSubDomainSingle() {
		String filteredDomain = CommunityFilter.removeSubDomains("localhost");
		assertThat(filteredDomain, equalTo("localhost"));
	}

	@Test
	public void testRemoveSubDomainDomain() {
		String filteredDomain = CommunityFilter.removeSubDomains("nicedomain.com");
		assertThat(filteredDomain, equalTo("nicedomain.com"));
	}

	@Test
	public void testRemoveSubDomainSubdomain() {
		String filteredDomain = CommunityFilter.removeSubDomains("www.nicedomain.com");
		assertThat(filteredDomain, equalTo("nicedomain.com"));
	}

	@Test
	public void testRemoveSubDomainTwoSubdomains() {
		String filteredDomain = CommunityFilter.removeSubDomains("more.some.nicedomain.com");
		assertThat(filteredDomain, equalTo("nicedomain.com"));
	}
}
