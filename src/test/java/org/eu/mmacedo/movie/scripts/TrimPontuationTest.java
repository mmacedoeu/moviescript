package org.eu.mmacedo.movie.scripts;

import static org.junit.Assert.*;

import org.junit.Test;

public class TrimPontuationTest {

	@Test
	public void test1() {
		String apply = Helper.trimPontuation.apply("I");
		assertNotNull(apply);
		assert("I".equals(apply));
	}
	
	@Test
	public void test2() {
		String apply = Helper.trimPontuation.apply("see,");
		assertNotNull(apply);
		assert("see".equals(apply));
	}	
	
	@Test
	public void test3() {
		String apply = Helper.trimPontuation.apply(",sir");
		assertNotNull(apply);
		assert("sir".equals(apply));
	}
	
	@Test
	public void test4() {
		String apply = Helper.trimPontuation.apply("It's");
		assertNotNull(apply);
		assert("It's".equals(apply));
	}		
	

}
