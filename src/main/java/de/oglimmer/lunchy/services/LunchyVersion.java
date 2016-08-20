package de.oglimmer.lunchy.services;

import javax.servlet.ServletContext;

import de.oglimmer.utils.VersionFromManifest;
import lombok.Getter;

public enum LunchyVersion {
	INSTANCE;

	@Getter
	private String version;
	@Getter
	private boolean runsOnDev;

	public void init(ServletContext context) {
		VersionFromManifest vfm = new VersionFromManifest();
		vfm.initFromFile(context.getRealPath("/META-INF/MANIFEST.MF"));
		version = vfm.getLongVersion();
		runsOnDev = vfm.isInitFailed();
	}

}
