package de.oglimmer.lunchy.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.BeforeClass;
import org.junit.Test;

public class ConfigBotDetectionServiceTest {

	@BeforeClass
	public static void startup() {
		System.setProperty("lunchy.properties", "memory:{\"app.bots\":[\"bot\",\"crawler\",\"spider\"],"
				+ "\"app.no-bots\":[\"Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)\"]}");
	}

	@Test
	public void googleDefault1() {
		boolean b = BotDetectionService.INSTANCE.isBot("Googlebot/2.1 (+http://www.google.com/bot.html)");
		assertThat(b, is(true));
	}

	@Test
	public void googleDefault2() {
		boolean b = BotDetectionService.INSTANCE
				.isBot("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");
		assertThat(b, is(false));
	}

	@Test
	public void bot1() {
		boolean b = BotDetectionService.INSTANCE
				.isBot("Mozilla/5.0 (compatible; Baiduspider/2.0; +http://www.baidu.com/search/spider.html)");
		assertThat(b, is(true));
	}

	@Test
	public void bot2() {
		boolean b = BotDetectionService.INSTANCE
				.isBot("Mozilla/5.0 (compatible; bingbot/2.0; +http://www.bing.com/bingbot.htm)");
		assertThat(b, is(true));
	}

	@Test
	public void bot3() {
		boolean b = BotDetectionService.INSTANCE
				.isBot("Mozilla/5.0 (compatible; meanpathbot/1.0; +http://www.meanpath.com/meanpathbot.html)");
		assertThat(b, is(true));
	}

	@Test
	public void bot4() {
		boolean b = BotDetectionService.INSTANCE
				.isBot("Mozilla/5.0 (compatible; Yahoo! Slurp; http://help.yahoo.com/help/us/ysearch/slurp)");
		assertThat(b, is(false));
	}

	@Test
	public void noBot1() {
		boolean b = BotDetectionService.INSTANCE.isBot("Mozilla/5.0 (Windows; U; MSIE 9.0; Windows NT 9.0; en-US");
		assertThat(b, is(false));
	}

	@Test
	public void noBot2() {
		boolean b = BotDetectionService.INSTANCE.isBot(
				"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.120 Safari/537.36");
		assertThat(b, is(false));
	}

}
