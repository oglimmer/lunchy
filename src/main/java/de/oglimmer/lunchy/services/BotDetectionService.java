package de.oglimmer.lunchy.services;

import java.util.regex.Pattern;

public enum BotDetectionService {
	INSTANCE;

	// as per https://support.google.com/webmasters/answer/1061943?hl=en
	private static final String GOOGLEBOT_1 = RegExService.INSTANCE
			.escape("Googlebot/2.1 (+http://www.google.com/bot.html)");
	private static final String GOOGLEBOT_2 = RegExService.INSTANCE
			.escape("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");

	public static final String DEFAULT_BOT_REGEX = ".*(bot|crawler|spider|yahoo! slurp).*";
	public static final String DEFAULT_NO_BOT_REGEX = ".*(" + BotDetectionService.GOOGLEBOT_1 + "|"
			+ BotDetectionService.GOOGLEBOT_2 + ").*";

	private final Pattern patBot = Pattern.compile(LunchyProperties.INSTANCE.getIsBot());
	private final Pattern patNoBot = Pattern.compile(LunchyProperties.INSTANCE.getIsNoBot());

	public boolean isBot(String userAgent) {
		if (userAgent == null || userAgent.isEmpty()) {
			return false;
		}
		String lowerCaseUserAgent = userAgent.toLowerCase().trim();

		// Some bots can understand Angular, we don't treat it as a bot and show the regular page
		if (patNoBot.matcher(lowerCaseUserAgent).matches()) {
			return false;
		}

		return patBot.matcher(lowerCaseUserAgent).matches();
	}

}
