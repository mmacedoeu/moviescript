package org.eu.mmacedo.movie.scripts;

import static org.junit.Assert.*;

import org.eu.mmacedo.movie.scripts.splitter.SettingSplitter;
import org.junit.Test;

public class SettingNameParserTest {

	@Test
	public void test1() {
		String line = "INT. REBEL BLOCKADE RUNNER - MAIN PASSAGEWAY\n";
		SettingSplitter ss = new SettingSplitter();
		String name = ss.nameParser.apply(line);
		assert("REBEL BLOCKADE RUNNER".equals(name));
	}
	
	@Test
	public void test2() {
		String line = "EXT. SPACECRAFT IN SPACE";
		SettingSplitter ss = new SettingSplitter();
		String name = ss.nameParser.apply(line);
		assert("SPACECRAFT IN SPACE".equals(name));
	}	
	
	@Test
	public void test3() {
		String line = "EXT. TATOOINE - ANCHORHEAD SETTLEMENT - POWER STATION - DAY";
		SettingSplitter ss = new SettingSplitter();
		String name = ss.nameParser.apply(line);
		assert("TATOOINE".equals(name));
	}		
	
	

}
