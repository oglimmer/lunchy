package de.oglimmer.lunchy.services;

import java.util.regex.Pattern;

public enum BotDetectionService {
	INSTANCE;

	static class Constancts {
		// as per https://support.google.com/webmasters/answer/1061943?hl=en
		private static final String GOOGLEBOT_1 = RegExService.INSTANCE
				.escape("Googlebot/2.1 (+http://www.google.com/bot.html)");
		private static final String GOOGLEBOT_2 = RegExService.INSTANCE
				.escape("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");

		public static final String DEFAULT_BOT_REGEX = ".*(bot|crawler|spider|yahoo! slurp).*";
		public static final String DEFAULT_NO_BOT_REGEX = ".*(" + BotDetectionService.Constancts.GOOGLEBOT_1 + "|"
				+ BotDetectionService.Constancts.GOOGLEBOT_2 + ").*";
	}

	private Pattern patBot;
	private Pattern patNoBot;

	private BotDetectionService() {
		initRegEx();
		LunchyProperties.INSTANCE.registerOnReload(this::initRegEx);
	}

	private void initRegEx() {
		patBot = Pattern.compile(LunchyProperties.INSTANCE.getReloadable().getIsBot());
		patNoBot = Pattern.compile(LunchyProperties.INSTANCE.getReloadable().getIsNoBot());
	}

	public boolean isBot(String userAgent) {
		if (userAgent == null || userAgent.isEmpty()) {
			return false;
		}
		String lowerCaseUserAgent = userAgent.toLowerCase().trim();

		// Some bots (e.g. google) can understand Angular, we don't treat it as a bot and show the regular page
		if (patNoBot.matcher(lowerCaseUserAgent).matches()) {
			return false;
		}

		return patBot.matcher(lowerCaseUserAgent).matches();
	}

}
