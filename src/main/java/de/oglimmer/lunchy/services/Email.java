package de.oglimmer.lunchy.services;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import de.oglimmer.lunchy.database.dao.CommunityDao;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;
import de.oglimmer.lunchy.rest.dto.UpdatesQuery;
import de.oglimmer.lunchy.services.EmailUpdatesNotifier.MailImage;

@Slf4j
public enum Email {
	INSTANCE;

	private static final String DOMAIN = "%s.lunchylunch.com";
	private static final String URL = "http://%s/lunchy";

	private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

	public void shutdown() {
		log.debug("Stopping Email scheduler...");
		executor.shutdown();
	}

	public String getUrl(int fkCommunity) {
		return String.format(URL, getDomain(fkCommunity));
	}

	public String getDomain(int fkCommunity) {
		return String.format(DOMAIN, CommunityDao.INSTANCE.getById(fkCommunity).getDomain());
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
		send(to, subject, body, null);
	}

	private void send(final String to, final String subject, String body, String htmlBody) {		
		try {
			final HtmlEmail email = setup();
			email.addTo(to);
			email.setSubject(subject);
			email.setHtmlMsg(htmlBody != null ? htmlBody : body);
			email.setTextMsg(body);

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

	public void sendUpdates(UsersRecord rec, List<UpdatesQuery> updates, List<MailImage> images) {
		String subject = "Lunchy weekly email updates";
		log.debug("Ready to send email to " + rec.getEmail() + " from " + rec.getLastEmailUpdate());
		send(rec.getEmail(), subject, NotificationEmailText.INSTANCE.getText(rec, updates, images),
				NotificationEmailText.INSTANCE.getHtml(rec, updates, images));
	}

}
