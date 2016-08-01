package org.eu.mmacedo.movie.scripts;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Collection;
import java.util.Scanner;

import org.eu.mmacedo.movie.scripts.domain.Setting;
import org.eu.mmacedo.movie.scripts.splitter.SettingSplitter;
import org.junit.Test;

public class SettingSplitterTest {

	@Test
	public void test1() throws FileNotFoundException {
		URL url = this.getClass().getResource("/settingsplittertest1.txt");
		File testFile = new File(url.getFile());
		Scanner scanner = new Scanner( testFile, "UTF-8" );
		String text = scanner.useDelimiter("\\A").next();
		scanner.close();		
				
		SettingSplitter ss = new SettingSplitter();
		Collection<Setting> output = ss.apply(text);
		
		assertNotNull(output);
		assertTrue(output.size() == 2);
		for (Setting setting : output) {
			assertNotNull(setting.getCharacter());
			assertNotNull(setting.getRaw());
			assertNotNull(setting.getName());
		}
	}

}
