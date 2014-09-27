package de.oglimmer.lunchy.services;

import javax.servlet.http.HttpServletRequest;

import de.oglimmer.lunchy.database.generated.tables.records.CommunitiesRecord;

public class CommunityService {

	private CommunityService() {
	}

	public static int get(HttpServletRequest request) {
		return ((CommunitiesRecord) request.getAttribute("community")).getId();
	}

	public static void set(HttpServletRequest request, CommunitiesRecord comRec) {
		request.setAttribute("community", comRec);
	}

}
