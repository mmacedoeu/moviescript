package org.eu.mmacedo.movie.scripts;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Collection;

import org.eu.mmacedo.movie.scripts.domain.MovieCharacter;
import org.eu.mmacedo.movie.scripts.domain.Setting;
import org.eu.mmacedo.movie.scripts.domain.Word;
import org.eu.mmacedo.movie.scripts.service.ScriptParser;
import org.eu.mmacedo.movie.scripts.splitter.SettingSplitter;
import org.junit.Test;

public class ScriptParserTest {

	@Test
	public void test1() throws IOException {
		final URL url = this.getClass().getResource("/ScriptParsertest1.txt");
		final File testFile = new File(url.getFile());

		final RandomAccessFile aFile = new RandomAccessFile(testFile, "r");
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

		final String text = new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
		aFile.close();

		final SettingSplitter ss = new SettingSplitter();
		final Collection<Setting> output = ss.apply(text);

		final ScriptParser sp = new ScriptParser();
		sp.characterBlanks = 22;
		sp.dialogBlanks = 10;

		final Setting ps = sp.apply(output.iterator().next());
		assertNotNull(ps);
		assertNotNull(ps.getName());
		assert ("LUKE'S SPEEDER".equals(ps.getName()));
		assertNotNull(ps.getCharacter());
		assert (ps.getCharMap().size() == 2);
		for (final MovieCharacter c : ps.getCharacter()) {
			assertNotNull(c.getName());
			assert ("THREEPIO".equals(c.getName()) || "LUKE".equals(c.getName()));

			if ("THREEPIO".equals(c.getName())) {
				Word word = c.getIndex().get("i");
				assertNotNull(word);
				assertNotNull(word.getCounter());
				assert (word.getCounter() == 2);

				word = c.getIndex().get("see");
				assertNotNull(word);
				assertNotNull(word.getCounter());
				assert (word.getCounter() == 2);

				word = c.getIndex().get("sir");
				assertNotNull(word);
				assertNotNull(word.getCounter());
				assert (word.getCounter() == 2);

				word = c.getIndex().get("luke");
				assertNotNull(word);
				assertNotNull(word.getCounter());
				assert (word.getCounter() == 1);

				assertNotNull(c.getIndex());
				assert (c.getIndex().size() == 4);

			} else if ("LUKE".equals(c.getName())) {
				Word word = c.getIndex().get("luke");
				assertNotNull(word);
				assertNotNull(word.getCounter());
				assert (word.getCounter() == 2);

				word = c.getIndex().get("uh");
				assertNotNull(word);
				assertNotNull(word.getCounter());
				assert (word.getCounter() == 1);

				word = c.getIndex().get("you");
				assertNotNull(word);
				assertNotNull(word.getCounter());
				assert (word.getCounter() == 1);

				word = c.getIndex().get("can");
				assertNotNull(word);
				assertNotNull(word.getCounter());
				assert (word.getCounter() == 1);

				word = c.getIndex().get("me");
				assertNotNull(word);
				assertNotNull(word.getCounter());
				assert (word.getCounter() == 1);

				word = c.getIndex().get("just");
				assertNotNull(word);
				assertNotNull(word.getCounter());
				assert (word.getCounter() == 1);

				assertNotNull(c.getIndex());
				assert (c.getIndex().size() == 7);
			}
		}

	}

}
