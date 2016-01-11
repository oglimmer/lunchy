package de.oglimmer.lunchy.email;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import de.oglimmer.lunchy.database.dao.CommunityDao;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;
import de.oglimmer.lunchy.rest.dto.MailImage;
import de.oglimmer.lunchy.rest.dto.UpdatesQuery;
import de.oglimmer.lunchy.services.LunchyProperties;

@Slf4j
public enum EmailProvider {
	INSTANCE;

	private static final String DOMAIN = LunchyProperties.INSTANCE.getDomain();
	private static final String URL = "http://%s";
	private static final String URLS = "https://%s";

	private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

	public void shutdown() {
		log.debug("Stopping Email scheduler...");
		executor.shutdown();
	}

	public String getUrl(int fkCommunity) {
		String domain = getDomain(fkCommunity);
		if (domain.matches(LunchyProperties.INSTANCE.getSecureDomainPattern())) {
			return String.format(URLS, domain);
		} else {
			return String.format(URL, domain);
		}
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

	private void send(final String to, final String subject, String body, final String htmlBody) {
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
							log.trace(htmlBody);
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
						+ "<br/><br/>You have requested a link to reset your Lunchy password.<br/><br/>Click here to reset your password: "
						+ getUrl(user.getFkCommunity())
						+ "/#/passwordReset?token="
						+ user.getPasswordResetToken()
						+ "<br/><br/>This link works for 24 hours.<br/><br/>You can ignore this email if you haven't requested this link.<br/><br/>Regards,<br/>Oli");
	}

	public void sendWelcome(String emailAddress, String name, int fkCommunity) {
		sendWelcomeUser(emailAddress, name, fkCommunity);
		sendWelcomeAdmin(name, fkCommunity);
	}

	public void sendWelcomeUser(String emailAddress, String name, int fkCommunity) {
		send(emailAddress, "Welcome to Lunchy", "Hello " + name
				+ "<br/><br/>You have successfully registered at lunchy.<br/><br/>Visit " + getUrl(fkCommunity)
				+ " to explore lunch places.<br/><br/>Regards,<br/>Oli");
	}

	public void sendWelcomeAdmin(String name, int fkCommunity) {
		send(CommunityDao.INSTANCE.getById(fkCommunity).getAdminEmail(), "A new user registered to Lunchy",
				"Hello admin<br/><br/>a new user " + name + " registered at lunchy. Go to " + getUrl(fkCommunity)
						+ "/#/user and set a permission.<br/><br/>Regards,<br/>Oli");
	}

	public void sendPasswordResetDone(UsersRecord user) {
		send(user.getEmail(), "Lunchy - Password reset done", "Hello " + user.getDisplayname()
				+ "<br/><br/>You have successfully reset your Lunchy password.<br/><br/>Regards,<br/>Oli");
	}

	public void sendUpdates(UsersRecord rec, List<UpdatesQuery> updates, List<MailImage> images) {
		String subject = "Lunchy weekly email updates";
		log.debug("Ready to send email to " + rec.getEmail() + " from " + rec.getLastEmailUpdate());
		send(rec.getEmail(), subject, NotificationEmailText.getText(rec, updates, images),
				NotificationEmailText.getHtml(rec, updates, images));
	}

}
