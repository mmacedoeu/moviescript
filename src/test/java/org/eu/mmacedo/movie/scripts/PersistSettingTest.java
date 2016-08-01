package org.eu.mmacedo.movie.scripts;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.eu.mmacedo.movie.scripts.domain.MovieCharacter;
import org.eu.mmacedo.movie.scripts.domain.Setting;
import org.eu.mmacedo.movie.scripts.domain.Word;
import org.eu.mmacedo.movie.scripts.repository.MovieCharacterRepository;
import org.eu.mmacedo.movie.scripts.repository.WordRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration("/META-INF/spring/integration/spring-persist1-context.xml")
@DatabaseSetup(PersistSettingTest.DATASET)
@DirtiesContext
public class PersistSettingTest {

	protected static final String DATASET = "classpath:datasets/set-items2.xml";

	@Autowired
	@Qualifier("persistRequest")
	MessageChannel persistRequest;

	@Autowired
	private WordRepository wordRepository;

	@Autowired
	private MovieCharacterRepository charRepository;

	@Autowired
	private DataSource datasource;

	@Test
	public void test1()
			throws SQLException, DatabaseUnitException, FileNotFoundException, IOException, InterruptedException {
		final Setting set = new Setting();
		set.setName("TATOOINE");
		set.setCharacter(new HashSet<>());

		final MovieCharacter cha = new MovieCharacter();
		cha.setName("LUKE");
		cha.setSetting(set);
		cha.setIndex(new HashMap<>());
		cha.setWords(new LinkedList<>());

		final Word w = new Word();
		w.setSetting(set.getName());
		w.setCharacter(cha);
		w.setWord("a");
		w.setCounter(2L);

		cha.getIndex().put("a", w);
		set.getCharacter().add(cha);

		final Message<Setting> reqmsg = new GenericMessage<>(set);
		persistRequest.send(reqmsg); // send message to process script in text

		TimeUnit.MILLISECONDS.sleep(200);

		// final IDatabaseConnection connection = new
		// DatabaseConnection(datasource.getConnection());
		// final IDataSet fullDataSet = connection.createDataSet();
		// FlatXmlDataSet.write(fullDataSet, new FileOutputStream("full.xml"));

		final List<Word> findBySettingByMovieCharacterByword = wordRepository
				.findBySettingByMovieCharacterByword("TATOOINE", "LUKE", "a");
		assertNotNull(findBySettingByMovieCharacterByword);
		assert (!findBySettingByMovieCharacterByword.isEmpty());
		assert (findBySettingByMovieCharacterByword.size() == 1);
		final Word word = findBySettingByMovieCharacterByword.get(0);
		assertNotNull(word);
		assertNotNull(word.getCounter());
		assertThat(word.getCounter() == 2L);
	}

	@Test
	public void test2()
			throws SQLException, DatabaseUnitException, FileNotFoundException, IOException, InterruptedException {
		final Setting set = new Setting();
		set.setName("REBEL BLOCKADE RUNNER");
		set.setCharacter(new HashSet<>());

		final MovieCharacter cha = new MovieCharacter();
		cha.setName("LUKE");
		cha.setSetting(set);
		cha.setWords(new LinkedList<>());
		cha.setIndex(new HashMap<>());

		final Word w = new Word();
		w.setSetting(set.getName());
		w.setCharacter(cha);
		w.setWord("luke");
		w.setCounter(5L);

		cha.getIndex().put("luke", w);
		set.getCharacter().add(cha);

		final Message<Setting> reqmsg = new GenericMessage<>(set);
		persistRequest.send(reqmsg); // send message to process script in text

		TimeUnit.MILLISECONDS.sleep(200);

		// final IDatabaseConnection connection = new
		// DatabaseConnection(datasource.getConnection());
		// final IDataSet fullDataSet = connection.createDataSet();
		// FlatXmlDataSet.write(fullDataSet, new FileOutputStream("full.xml"));

		final List<Word> findBySettingByMovieCharacterByword = wordRepository
				.findBySettingByMovieCharacterByword("REBEL BLOCKADE RUNNER", "LUKE", "luke");
		assertNotNull(findBySettingByMovieCharacterByword);
		assert (!findBySettingByMovieCharacterByword.isEmpty());
		assert (findBySettingByMovieCharacterByword.size() == 1);
		final Word word = findBySettingByMovieCharacterByword.get(0);
		assertNotNull(word);
		assertNotNull(word.getCounter());
		assertThat(word.getCounter() == 5L);
	}

	@Test
	public void test3()
			throws SQLException, DatabaseUnitException, FileNotFoundException, IOException, InterruptedException {
		final Setting set = new Setting();
		set.setName("REBEL BLOCKADE RUNNER");
		set.setCharacter(new HashSet<>());

		final MovieCharacter cha = new MovieCharacter();
		cha.setName("THREEPIO");
		cha.setSetting(set);
		cha.setWords(new LinkedList<>());
		cha.setIndex(new HashMap<>());

		final Word w = new Word();
		w.setSetting(set.getName());
		w.setCharacter(cha);
		w.setWord("sir");
		w.setCounter(3L);

		cha.getIndex().put("sir", w);
		set.getCharacter().add(cha);

		final Message<Setting> reqmsg = new GenericMessage<>(set);
		persistRequest.send(reqmsg); // send message to process script in text

		TimeUnit.MILLISECONDS.sleep(200);

		// final IDatabaseConnection connection = new
		// DatabaseConnection(datasource.getConnection());
		// final IDataSet fullDataSet = connection.createDataSet();
		// FlatXmlDataSet.write(fullDataSet, new FileOutputStream("full.xml"));

		final List<Word> findBySettingByMovieCharacterByword = wordRepository
				.findBySettingByMovieCharacterByword("REBEL BLOCKADE RUNNER", "THREEPIO", "sir");
		assertNotNull(findBySettingByMovieCharacterByword);
		assert (!findBySettingByMovieCharacterByword.isEmpty());
		assert (findBySettingByMovieCharacterByword.size() == 1);
		final Word word = findBySettingByMovieCharacterByword.get(0);
		assertNotNull(word);
		assertNotNull(word.getCounter());
		assertThat(word.getCounter() == 3L);
	}

	@Test
	public void test4()
			throws SQLException, DatabaseUnitException, FileNotFoundException, IOException, InterruptedException {
		final Setting set = new Setting();
		set.setName("REBEL BLOCKADE RUNNER");
		set.setCharacter(new HashSet<>());

		final MovieCharacter cha = new MovieCharacter();
		cha.setName("THREEPIO");
		cha.setSetting(set);
		cha.setWords(new LinkedList<>());
		cha.setIndex(new HashMap<>());

		final Word w = new Word();
		w.setSetting(set.getName());
		w.setCharacter(cha);
		w.setWord("a");
		w.setCounter(1L);

		cha.getIndex().put("a", w);
		set.getCharacter().add(cha);

		final Message<Setting> reqmsg = new GenericMessage<>(set);
		persistRequest.send(reqmsg); // send message to process script in text

		TimeUnit.MILLISECONDS.sleep(200);
		// final IDatabaseConnection connection = new
		// DatabaseConnection(datasource.getConnection());
		// final IDataSet fullDataSet = connection.createDataSet();
		// FlatXmlDataSet.write(fullDataSet, new FileOutputStream("full.xml"));

		final List<Word> findBySettingByMovieCharacterByword = wordRepository
				.findBySettingByMovieCharacterByword("REBEL BLOCKADE RUNNER", "THREEPIO", "a");
		assertNotNull(findBySettingByMovieCharacterByword);
		assert (!findBySettingByMovieCharacterByword.isEmpty());
		assert (findBySettingByMovieCharacterByword.size() == 1);
		final Word word = findBySettingByMovieCharacterByword.get(0);
		assertNotNull(word);
		assertNotNull(word.getCounter());
		assertThat(word.getCounter() == 8L);
	}

	@Test
	public void test5()
			throws SQLException, DatabaseUnitException, FileNotFoundException, IOException, InterruptedException {
		final Setting set = new Setting();
		set.setName("REBEL BLOCKADE RUNNER");
		set.setCharacter(new HashSet<>());

		final MovieCharacter cha = new MovieCharacter();
		cha.setName("LUKE");
		cha.setSetting(set);
		cha.setWords(new LinkedList<>());
		cha.setIndex(new HashMap<>());

		final Word w = new Word();
		w.setSetting(set.getName());
		w.setCharacter(cha);
		w.setWord("luke");
		w.setCounter(5L);

		cha.getIndex().put("luke", w);
		set.getCharacter().add(cha);

		final Message<Setting> reqmsg = new GenericMessage<>(set);
		persistRequest.send(reqmsg); // send message to process script in text

		TimeUnit.MILLISECONDS.sleep(200);
		// final IDatabaseConnection connection = new
		// DatabaseConnection(datasource.getConnection());
		// final IDataSet fullDataSet = connection.createDataSet();
		// FlatXmlDataSet.write(fullDataSet, new FileOutputStream("full.xml"));

		final List<MovieCharacter> findRootByName = charRepository.findRootByName("LUKE");
		assertNotNull(findRootByName);
		assert (findRootByName.size() == 1);
		final MovieCharacter movieCharacter = findRootByName.get(0);
		assert ("LUKE".equals(movieCharacter.getName()));
		final List<Word> words = movieCharacter.getWords();
		assertNotNull(words);
		assert (words.size() == 1);
		final Word word = words.get(0);
		assertNotNull(word);
		assert ("luke".equals(word.getWord()));
		assert (word.getCounter() == 5L);
	}

	private final BiFunction<Collection<Word>, String, Optional<Word>> containsName = (c, n) -> {
		if (n == null)
			return Optional.empty();
		for (final Word word : c) {
			if (n.equals(word.getWord()))
				return Optional.of(word);
		}
		return Optional.empty();
	};

	@Test
	public void test6()
			throws SQLException, DatabaseUnitException, FileNotFoundException, IOException, InterruptedException {
		final Setting set = new Setting();
		set.setName("REBEL BLOCKADE RUNNER");
		set.setCharacter(new HashSet<>());

		final MovieCharacter cha = new MovieCharacter();
		cha.setName("THREEPIO");
		cha.setSetting(set);
		cha.setWords(new LinkedList<>());
		cha.setIndex(new HashMap<>());

		final Word w = new Word();
		w.setSetting(set.getName());
		w.setCharacter(cha);
		w.setWord("sir");
		w.setCounter(5L);

		cha.getIndex().put("sir", w);
		set.getCharacter().add(cha);

		final Message<Setting> reqmsg = new GenericMessage<>(set);
		persistRequest.send(reqmsg); // send message to process script in text

		TimeUnit.MILLISECONDS.sleep(200);

		// final IDatabaseConnection connection = new
		// DatabaseConnection(datasource.getConnection());
		// final IDataSet fullDataSet = connection.createDataSet();
		// FlatXmlDataSet.write(fullDataSet, new FileOutputStream("full.xml"));

		final List<MovieCharacter> findRootByName = charRepository.findRootByName("THREEPIO");
		assertNotNull(findRootByName);
		assert (findRootByName.size() == 1);
		final MovieCharacter movieCharacter = findRootByName.get(0);
		assert ("THREEPIO".equals(movieCharacter.getName()));
		final List<Word> words = movieCharacter.getWords();
		assertNotNull(words);
		assert (words.size() == 2);
		final Optional<Word> apply = this.containsName.apply(words, "sir");
		assert (apply.isPresent());
		assert (apply.get().getCounter() == 5L);
	}

	@Test
	public void test7()
			throws SQLException, DatabaseUnitException, FileNotFoundException, IOException, InterruptedException {
		final Setting set = new Setting();
		set.setName("REBEL BLOCKADE RUNNER");
		set.setCharacter(new HashSet<>());

		final MovieCharacter cha = new MovieCharacter();
		cha.setName("THREEPIO");
		cha.setSetting(set);
		cha.setWords(new LinkedList<>());
		cha.setIndex(new HashMap<>());

		final Word w = new Word();
		w.setSetting(set.getName());
		w.setCharacter(cha);
		w.setWord("a");
		w.setCounter(5L);

		cha.getIndex().put("a", w);
		set.getCharacter().add(cha);

		final Message<Setting> reqmsg = new GenericMessage<>(set);
		persistRequest.send(reqmsg); // send message to process script in text

		TimeUnit.MILLISECONDS.sleep(200);
		// final IDatabaseConnection connection = new
		// DatabaseConnection(datasource.getConnection());
		// final IDataSet fullDataSet = connection.createDataSet();
		// FlatXmlDataSet.write(fullDataSet, new FileOutputStream("full.xml"));

		final List<MovieCharacter> findRootByName = charRepository.findRootByName("THREEPIO");
		assertNotNull(findRootByName);
		assert (findRootByName.size() == 1);
		final MovieCharacter movieCharacter = findRootByName.get(0);
		assert ("THREEPIO".equals(movieCharacter.getName()));
		final List<Word> words = movieCharacter.getWords();
		assertNotNull(words);
		assert (words.size() == 1);
		final Optional<Word> apply = this.containsName.apply(words, "a");
		assert (apply.isPresent());
		assert (apply.get().getCounter() == 12L);
	}

}
