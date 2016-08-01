package org.eu.mmacedo.movie.scripts;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.function.Function;

public class TestHelper {
	public static Function<String, String> readFile = p -> {
		final URL url = TestHelper.class.getResource(p);
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
}
