package de.oglimmer.lunchy.services;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import org.jooq.Record;

import de.oglimmer.lunchy.beanMapping.DozerAdapter;
import de.oglimmer.lunchy.database.dao.UpdatesDao;
import de.oglimmer.lunchy.database.dao.UserDao;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;
import de.oglimmer.lunchy.rest.dto.UpdatesQuery;
import de.oglimmer.lunchy.rest.resources.UpdatesResource;

@Slf4j
@ToString(exclude = "executorService")
public enum EmailUpdatesNotifier {
	INSTANCE;

	private ScheduledExecutorService executorService;

	private int checksDone = 0;
	private int emailsSent = 0;

	public void startUp() {
		if (LunchyProperties.INSTANCE.isEmailNotificationEnabled()) {
			log.debug("Starting EmailUpdatesNotifier...");
			executorService = Executors.newSingleThreadScheduledExecutor();
			executorService.scheduleAtFixedRate(new Check(), 0, 15, TimeUnit.SECONDS);
		}
	}

	public void shutdown() {
		if (executorService != null) {
			log.debug("Stopping EmailUpdatesNotifier...");
			executorService.shutdown();
		}
	}

	class Check implements Runnable {

		@Override
		public void run() {
			try {
				checksDone++;
				List<UsersRecord> list = UserDao.INSTANCE.getReadyForNotification();
				for (UsersRecord rec : list) {
					if (UserDao.INSTANCE.updateLastEmailUpdateTimeStamp(rec)) {
						emailsSent++;
						log.debug("{} will get update, last was {}, next is {}", rec.getEmail(),
								DateFormat.getDateTimeInstance().format(rec.getLastEmailUpdate()),
								DateFormat.getDateTimeInstance().format(rec.getNextEmailUpdate()));
						String updates = buildUpdates(rec);
						Email.INSTANCE.sendUpdates(rec, updates);
						rec.setNextEmailUpdate(calcNextUpdate(rec));
						UserDao.INSTANCE.store(rec);
					} else {
						log.warn("{} wont update since it was already touched by other process", rec.getEmail());
					}
				}
			} catch (Exception e) {
				log.error("Failed to process email notifications", e);
			}
		}

		private String buildUpdates(UsersRecord user) {
			StringBuilder buff = new StringBuilder();
			List<Record> updates = UpdatesDao.INSTANCE.get(user.getLastEmailUpdate(), user.getFkCommunity());
			UpdatesResource up = new UpdatesResource();
			for (Record rec : updates) {
				UpdatesQuery uq = up.createResultRow(new DozerAdapter(rec));
				buff.append("=> ").append(uq.getText()).append("\r\n");
			}
			return buff.toString();
		}

		private Timestamp calcNextUpdate(UsersRecord rec) {

			Calendar nextUpdate = null;
			switch (rec.getEmailUpdates()) {
			case 0:
				nextUpdate = DateCalculation.INSTANCE.findNextMonday();
				break;
			case 1:
				nextUpdate = DateCalculation.INSTANCE.findNever();
				log.error("User {} had EmailUpdate=1 but was selected for email sending", rec.getEmail());
				break;
			default:
				throw new RuntimeException("Illegal nextUpdate=" + rec.getEmailUpdates());
			}

			log.debug("Next update for {} is {}", rec.getEmail(), DateFormat.getDateTimeInstance().format(nextUpdate.getTime()));
			return new Timestamp(nextUpdate.getTime().getTime());
		}

	}

}
