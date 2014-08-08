package de.oglimmer.lunchy.services;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;

@Slf4j
public enum Email {
	INSTANCE;

	private static final String URL = "http://lunchy.oglimmer.de/lunchy";

	private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

	public void shutdown() {
		executor.shutdown();
	}

	private org.apache.commons.mail.Email setup() throws EmailException {
		org.apache.commons.mail.Email simpleEmail = new SimpleEmail();
		simpleEmail.setHostName("localhost");

		simpleEmail.setFrom("no-reply@junta-online.net");
		return simpleEmail;
	}

	private void send(final org.apache.commons.mail.Email simpleEmail) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					simpleEmail.send();
				} catch (EmailException e) {
					log.error("Failed to send email", e);
				}
			}
		});
	}

	public void sendPasswordLink(UsersRecord user) {
		try {
			org.apache.commons.mail.Email email = setup();
			email.setSubject("Lunchy - Reset your password link");
			email.setMsg("Hello "
					+ user.getDisplayname()
					+ "\r\n\r\nYou have requested a link to reset your Lunchy password.\r\n\r\nClick here to reset your password: "
					+ URL
					+ "/#/passwordReset?token="
					+ user.getPasswordresettoken()
					+ "\r\n\r\nThis link works for 24 hours.\r\n\r\nYou can ignore this email if you haven't requested this link.\r\n\r\nRegards,\r\nOli");
			email.addTo(user.getEmail());
			send(email);
		} catch (EmailException e) {
			log.error("Failed to send password email", e);
		}
	}

	public void sendWelcome(String emailAddress, String name) {
		try {
			org.apache.commons.mail.Email email = setup();
			email.setSubject("Welcome to Lunchy");
			email.setMsg("Hello " + name + "\r\n\r\nYou have successfully registered at lunchy.\r\n\r\nVisit " + URL
					+ " to explore lunch places.\r\n\r\nRegards,\r\nOli");
			email.addTo(emailAddress);
			send(email);
		} catch (EmailException e) {
			log.error("Failed to send password email", e);
		}
	}

	public void sendPasswordResetDone(UsersRecord user) {
		try {
			org.apache.commons.mail.Email email = setup();
			email.setSubject("Lunchy - Password reset done");
			email.setMsg("Hello " + user.getDisplayname()
					+ "\r\n\r\nYou have successfully reset your Lunchy password.\r\n\r\nRegards,\r\nOli");
			email.addTo(user.getEmail());
			send(email);
		} catch (EmailException e) {
			log.error("Failed to send password email", e);
		}
	}

}
