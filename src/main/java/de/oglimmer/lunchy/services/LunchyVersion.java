package de.oglimmer.lunchy.services;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.RandomStringUtils;

import lombok.Getter;

public enum LunchyVersion {
	INSTANCE;

	@Getter
	private String version;
	@Getter
	private String commit;
	@Getter
	private String lunchyVersion;
	@Getter
	private String creationDate;

	public void init(ServletContext context) {
		try (InputStream is = new FileInputStream(context.getRealPath("/META-INF/MANIFEST.MF"))) {
			Manifest mf = new Manifest(is);
			Attributes attr = mf.getMainAttributes();
			commit = attr.getValue("Git-Commit");
			lunchyVersion = attr.getValue("Lunchy-Version");
			long time = Long.parseLong(attr.getValue("Creation-Date"));
			creationDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(new Date(time));
		} catch (Exception e) {
			commit = RandomStringUtils.randomAlphanumeric(8);
			creationDate = "?";
			lunchyVersion = "?";
		}

		version = "V" + lunchyVersion + " [Commit#" + commit + "] build " + creationDate;
	}

}
