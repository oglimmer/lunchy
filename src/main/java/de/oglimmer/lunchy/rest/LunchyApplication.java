package de.oglimmer.lunchy.rest;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("rest")
public class LunchyApplication extends ResourceConfig {

	public LunchyApplication() {
		property("jersey.config.server.monitoring.statistics.mbeans.enabled", "true");
	}

}
