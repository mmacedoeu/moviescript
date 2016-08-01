package org.eu.mmacedo.movie.scripts;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.eu.mmacedo.movie.scripts.domain.MovieCharacter;
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
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = { ApplicationTest3.class })
@DirtiesContext
public class IntegrationTest1 {

	@Autowired
	private DataSource datasource;

	@Autowired
	private WordRepository wordRepository;

	@Autowired
	private MovieCharacterRepository charRepository;

	@Autowired
	@Qualifier("scriptRequest")
	MessageChannel scriptRequest;

	public Function<String, String> readFile = p -> {
		final URL url = this.getClass().getResource(p);
		final File testFile = new File(url.getFile());
		String text = null;
		RandomAccessFile aFile = null;
		try {
			aFile = new RandomAccessFile(testFile, "r");
			final FileChannel inChannel = aFile.getChannel();
			final long fileSize = inChannel.size();
			final ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);
			inChannel.read(buffer);
			byte[] bytes;
			if (buffer.hasArray()) {
				bytes = buffer.array();
			} else {
				bytes = new byte[buffer.remaining()];
				buffer.get(bytes);
			}
			text = new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (aFile != null)
				try {
					aFile.close();
				} catch (final IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

		return text;
	};

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
	public void test1() throws IOException, InterruptedException, DatabaseUnitException, SQLException {
		final String path = "/integrationtest1.txt";

		final String text = this.readFile.apply(path);
		final Message<String> reqmsg = new GenericMessage<>(text);
		scriptRequest.send(reqmsg); // send message to process script in text

		TimeUnit.SECONDS.sleep(1);

		// final IDatabaseConnection connection = new
		// DatabaseConnection(datasource.getConnection());
		// final IDataSet fullDataSet = connection.createDataSet();
		// FlatXmlDataSet.write(fullDataSet, new FileOutputStream("full.xml"));

		final List<Word> findBySettingByMovieCharacterByword = wordRepository
				.findBySettingByMovieCharacterByword("REBEL BLOCKADE RUNNER", "THREEPIO", "the");
		assertNotNull(findBySettingByMovieCharacterByword);
		assert (!findBySettingByMovieCharacterByword.isEmpty());
		assert (findBySettingByMovieCharacterByword.size() == 1);
		final Word word = findBySettingByMovieCharacterByword.get(0);
		assertNotNull(word);
		assertNotNull(word.getCounter());
		assert (word.getCounter() == 2L);

		final List<MovieCharacter> findRootByName = charRepository.findRootByName("THREEPIO");
		assertNotNull(findRootByName);
		assert (!findRootByName.isEmpty());
		assert (findRootByName.size() == 1);
		final MovieCharacter movieCharacter = findRootByName.get(0);
		assert (movieCharacter.getWords().size() == 26);
		final Optional<Word> apply = this.containsName.apply(movieCharacter.getWords(), "the");
		assert (apply.isPresent());
		assert (apply.get().getCounter() == 2L);
	}

	// @Test
	public void test2() throws IOException, InterruptedException, DatabaseUnitException, SQLException {
		final String path = "/integrationtest2.txt";

		final String text = this.readFile.apply(path);
		final Message<String> reqmsg = new GenericMessage<>(text);
		scriptRequest.send(reqmsg); // send message to process script in text

		TimeUnit.SECONDS.sleep(1);

		// final IDatabaseConnection connection = new
		// DatabaseConnection(datasource.getConnection());
		// final IDataSet fullDataSet = connection.createDataSet();
		// FlatXmlDataSet.write(fullDataSet, new FileOutputStream("full.xml"));

		final List<Word> findBySettingByMovieCharacterByword = wordRepository
				.findBySettingByMovieCharacterByword("REBEL BLOCKADE RUNNER", "THREEPIO", "the");
		assertNotNull(findBySettingByMovieCharacterByword);
		assert (!findBySettingByMovieCharacterByword.isEmpty());
		assert (findBySettingByMovieCharacterByword.size() == 1);
		final Word word = findBySettingByMovieCharacterByword.get(0);
		assertNotNull(word);
		assertNotNull(word.getCounter());
		assert (word.getCounter() == 3L);

		final List<MovieCharacter> findRootByName = charRepository.findRootByName("THREEPIO");
		assertNotNull(findRootByName);
		assert (!findRootByName.isEmpty());
		assert (findRootByName.size() == 1);
		final MovieCharacter movieCharacter = findRootByName.get(0);
		assert (movieCharacter.getWords().size() == 41);
		final Optional<Word> apply = this.containsName.apply(movieCharacter.getWords(), "the");
		assert (apply.isPresent());
		assert (apply.get().getCounter() == 3L);
	}

	@Test
	public void test3() throws IOException, InterruptedException, DatabaseUnitException, SQLException {
		final String path = "/integrationtest3.txt";

		final String text = this.readFile.apply(path);
		final Message<String> reqmsg = new GenericMessage<>(text);
		scriptRequest.send(reqmsg); // send message to process script in text

		TimeUnit.SECONDS.sleep(1);

		// final IDatabaseConnection connection = new
		// DatabaseConnection(datasource.getConnection());
		// final IDataSet fullDataSet = connection.createDataSet();
		// FlatXmlDataSet.write(fullDataSet, new FileOutputStream("full.xml"));

		final List<Word> findBySettingByMovieCharacterByword = wordRepository
				.findBySettingByMovieCharacterByword("TATOOINE", "LUKE", "there");
		assertNotNull(findBySettingByMovieCharacterByword);
		assert (!findBySettingByMovieCharacterByword.isEmpty());
		assert (findBySettingByMovieCharacterByword.size() == 1);
		final Word word = findBySettingByMovieCharacterByword.get(0);
		assertNotNull(word);
		assertNotNull(word.getCounter());
		assert (word.getCounter() == 2L);

		final List<MovieCharacter> findRootByName = charRepository.findRootByName("LUKE");
		assertNotNull(findRootByName);
		assert (!findRootByName.isEmpty());
		assert (findRootByName.size() == 1);
		final MovieCharacter movieCharacter = findRootByName.get(0);
		final Optional<Word> apply = this.containsName.apply(movieCharacter.getWords(), "there");
		assert (apply.isPresent());
		assert (apply.get().getCounter() == 3L);

	}

	@Test
	public void test4() throws IOException, InterruptedException, DatabaseUnitException, SQLException {
		final String path = "/screenplay_(2).txt";

		final String text = this.readFile.apply(path);
		final Message<String> reqmsg = new GenericMessage<>(text);
		scriptRequest.send(reqmsg); // send message to process script in text

		TimeUnit.SECONDS.sleep(60);

		assert (true);

	}

}
