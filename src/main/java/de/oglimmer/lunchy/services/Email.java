package de.oglimmer.lunchy.services;

import java.text.DateFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import de.oglimmer.lunchy.database.dao.CommunityDao;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;

@Slf4j
public enum Email {
	INSTANCE;

	private static final String URL = "http://%s.lunchylunch.com/lunchy";

	private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

	public void shutdown() {
		log.debug("Stopping Email scheduler...");
		executor.shutdown();
	}

	private String getUrl(int fkCommunity) {
		return String.format(URL, CommunityDao.INSTANCE.getById(fkCommunity).getDomain());
	}

	private HtmlEmail setup() throws EmailException {
		HtmlEmail simpleEmail = new HtmlEmail();
		simpleEmail.setHostName(LunchyProperties.INSTANCE.getSmtpHost());
		if (LunchyProperties.INSTANCE.getSmtpPort() != -1) {
			simpleEmail.setSmtpPort(LunchyProperties.INSTANCE.getSmtpPort());
			simpleEmail.setSslSmtpPort(Integer.toString(LunchyProperties.INSTANCE.getSmtpPort()));
		}
		if (!LunchyProperties.INSTANCE.getSmtpUser().isEmpty()) {
			simpleEmail.setAuthentication(LunchyProperties.INSTANCE.getSmtpUser(), LunchyProperties.INSTANCE.getSmtpPassword());
		}
		simpleEmail.setSSLOnConnect(LunchyProperties.INSTANCE.getSmtpSSL());

		simpleEmail.setFrom(LunchyProperties.INSTANCE.getSmtpFrom());

		return simpleEmail;
	}

	private void send(final String to, final String subject, final String body) {
		try {
			final HtmlEmail email = setup();
			email.addTo(to);
			email.setSubject(subject);
			email.addPart(body, "text/plain;charset=utf8");
			if (!LunchyProperties.INSTANCE.isEmailDisabled()) {
				executor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							log.debug("Email (truely) sent to {} with subject {}", to, subject);
							email.send();
						} catch (EmailException e) {
							log.error("Failed to send email", e);
						}
					}
				});
			}
		} catch (EmailException e) {
			log.error("Failed to create HtmlEmail", e);
		}
	}

	public void sendPasswordLink(UsersRecord user) {
		send(user.getEmail(),
				"Lunchy - Reset your password link",
				"Hello "
						+ user.getDisplayname()
						+ "\r\n\r\nYou have requested a link to reset your Lunchy password.\r\n\r\nClick here to reset your password: "
						+ getUrl(user.getFkCommunity())
						+ "/#/passwordReset?token="
						+ user.getPasswordResetToken()
						+ "\r\n\r\nThis link works for 24 hours.\r\n\r\nYou can ignore this email if you haven't requested this link.\r\n\r\nRegards,\r\nOli");
	}

	public void sendWelcome(String emailAddress, String name, int fkCommunity) {
		sendWelcomeUser(emailAddress, name, fkCommunity);
		sendWelcomeAdmin(name, fkCommunity);
	}

	public void sendWelcomeUser(String emailAddress, String name, int fkCommunity) {
		send(emailAddress, "Welcome to Lunchy", "Hello " + name + "\r\n\r\nYou have successfully registered at lunchy.\r\n\r\nVisit "
				+ getUrl(fkCommunity) + " to explore lunch places.\r\n\r\nRegards,\r\nOli");
	}

	public void sendWelcomeAdmin(String name, int fkCommunity) {
		send(CommunityDao.INSTANCE.getById(fkCommunity).getAdminEmail(), "A new user registered to Lunchy",
				"Hello admin\r\n\r\na new user " + name + " registered at lunchy. Go to " + getUrl(fkCommunity)
						+ "/#/user and set a permission.\r\n\r\nRegards,\r\nOli");
	}

	public void sendPasswordResetDone(UsersRecord user) {
		send(user.getEmail(), "Lunchy - Password reset done", "Hello " + user.getDisplayname()
				+ "\r\n\r\nYou have successfully reset your Lunchy password.\r\n\r\nRegards,\r\nOli");
	}

	public void sendUpdates(UsersRecord rec, String updates) {
		String CR = "\r\n";
		String subject = "Lunchy weekly email updates";
		StringBuilder body = new StringBuilder();
		body.append("Hello " + rec.getDisplayname() + " my hungry friend," + CR);
		body.append(CR);
		body.append("here are the updates from " + getUrl(rec.getFkCommunity()) + " starting from "
				+ DateFormat.getDateInstance().format(rec.getLastEmailUpdate()) + " :");
		body.append(CR);
		if (updates.isEmpty()) {
			body.append(CR + "Nothing happened this week :( - visit lunchy now and enter new locations, reviews and pictures!" + CR);
		} else {
			body.append(updates);
		}
		body.append(CR);
		body.append(CR);
		body.append("Regards," + CR);
		body.append("Oli" + CR);
		body.append(CR);
		body.append(CR);
		body.append("To unsubscribe from this email change your settings at " + getUrl(rec.getFkCommunity()));

		log.debug("Updates to " + rec.getEmail() + " from " + rec.getLastEmailUpdate() + " scheduled.");
		send(rec.getEmail(), subject, body.toString());
	}
}
