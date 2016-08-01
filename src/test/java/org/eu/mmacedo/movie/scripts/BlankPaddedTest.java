package org.eu.mmacedo.movie.scripts;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class BlankPaddedTest {

	@Test
	public void test1() {
		char[] array = new char[10];
		Arrays.fill(array, ' ');
		String space = new String(array);
		assert(Helper.isBlankPadded.test(space + "a", 10));		
	}
	
	@Test
	public void test2() {
		char[] array = new char[10];
		Arrays.fill(array, ' ');
		String space = new String(array);
		assert(!Helper.isBlankPadded.test(space, 12));		
	}	

	@Test
	public void test3() {
		char[] array = new char[1];
		Arrays.fill(array, ' ');
		String space = new String(array);
		assert(!Helper.isBlankPadded.test(space, 1));		
	}
	
	@Test
	public void test4() {
		char[] array = new char[1];
		Arrays.fill(array, ' ');
		String space = new String(array);
		assert(Helper.isBlankPadded.test(space + "a", 1));		
	}	

	@Test
	public void test5() {
		char[] array = new char[10];
		Arrays.fill(array, ' ');
		String space = new String(array);
		assert(!Helper.isBlankPadded.test(space, 1));		
	}	
	
	
}
