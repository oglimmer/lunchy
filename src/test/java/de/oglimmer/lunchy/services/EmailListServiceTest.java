package de.oglimmer.lunchy.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import lombok.SneakyThrows;

/*
 * These tests only work with the file based implementation
 */
@Ignore
public class EmailListServiceTest {

	private static final int LOC_NUMBER = 20;
	private static final String TMP_EMAIL_LIST_SERVICE_TEST_JSON = System.getProperty("java.io.tmpdir")
			+ "/EmailListServiceTest.json";
	private static final int USER_ID_ADD_BY_DEFAULT = 10;

	@SneakyThrows(value = IOException.class)
	@Before
	public void startUp() {
		System.setProperty("lunchy-emaillist-service", "memory:{ \"location." + LOC_NUMBER + "\": { \"usersFile\": \""
				+ TMP_EMAIL_LIST_SERVICE_TEST_JSON + "\" } }");
		Files.write(Paths.get(TMP_EMAIL_LIST_SERVICE_TEST_JSON), "".getBytes());
	}

	@After
	public void tearDown() {
		new File(TMP_EMAIL_LIST_SERVICE_TEST_JSON).delete();
	}

	@Test
	public void testIn() {
		EmailListService.INSTANCE.add(LOC_NUMBER, USER_ID_ADD_BY_DEFAULT);
		assertThat(EmailListService.INSTANCE.isUserEnabled(LOC_NUMBER, USER_ID_ADD_BY_DEFAULT), is(true));
		assertThat(EmailListService.INSTANCE.isUserEnabled(LOC_NUMBER, USER_ID_ADD_BY_DEFAULT + 1), is(false));
	}

	@Test
	public void testAddSimple() {
		int userIdToAdd = USER_ID_ADD_BY_DEFAULT + 1;
		EmailListService.INSTANCE.add(LOC_NUMBER, USER_ID_ADD_BY_DEFAULT);
		assertThat(EmailListService.INSTANCE.isUserEnabled(LOC_NUMBER, userIdToAdd), is(false));
		assertThat(EmailListService.INSTANCE.isUserEnabled(LOC_NUMBER, USER_ID_ADD_BY_DEFAULT), is(true));
		EmailListService.INSTANCE.add(LOC_NUMBER, userIdToAdd);
		assertThat(EmailListService.INSTANCE.isUserEnabled(LOC_NUMBER, userIdToAdd), is(true));
		assertThat(EmailListService.INSTANCE.isUserEnabled(LOC_NUMBER, USER_ID_ADD_BY_DEFAULT), is(true));
	}

	@Test
	public void testRemoveSimple() {
		EmailListService.INSTANCE.add(LOC_NUMBER, USER_ID_ADD_BY_DEFAULT);
		assertThat(EmailListService.INSTANCE.isUserEnabled(LOC_NUMBER, USER_ID_ADD_BY_DEFAULT), is(true));
		EmailListService.INSTANCE.remove(LOC_NUMBER, USER_ID_ADD_BY_DEFAULT);
		assertThat(EmailListService.INSTANCE.isUserEnabled(LOC_NUMBER, USER_ID_ADD_BY_DEFAULT), is(false));
	}

	@Test
	public void testAddRemove() {
		List<Integer> list = new ArrayList<>();
		while (list.size() < 1000) {
			addNumber(list);
		}
		while (!list.isEmpty()) {
			removeNumber(list);
		}
	}

	@Test
	public void testRandomAddRemove() {
		List<Integer> list = new ArrayList<>();
		int i = 0;
		while (i++ < 1000) {
			addNumber(list);
			removeNumber(list);
		}

	}

	private void removeNumber(List<Integer> list) {
		int index = (int) (list.size() * Math.random());
		int numberToRemove = list.get(index);
		list.remove(index);
		assertThat(EmailListService.INSTANCE.isUserEnabled(LOC_NUMBER, numberToRemove), is(true));
		EmailListService.INSTANCE.remove(LOC_NUMBER, numberToRemove);
		assertThat(EmailListService.INSTANCE.isUserEnabled(LOC_NUMBER, numberToRemove), is(false));
	}

	private void addNumber(List<Integer> list) {
		int numberToAdd = (int) (Math.random() * 1000);
		if (!list.contains(numberToAdd)) {
			list.add(numberToAdd);
			assertThat(EmailListService.INSTANCE.isUserEnabled(LOC_NUMBER, numberToAdd), is(false));
			EmailListService.INSTANCE.add(LOC_NUMBER, numberToAdd);
			assertThat(EmailListService.INSTANCE.isUserEnabled(LOC_NUMBER, numberToAdd), is(true));
		}
	}

}
