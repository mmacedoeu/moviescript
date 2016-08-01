package org.eu.mmacedo.movie.scripts;

import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class Helper {
	public static Predicate<String> isAllUpperCase = s -> {
		for (int i=0; i<s.length(); i++)
		{
			if (!Character.isUpperCase(s.charAt(i)))
			{
				return false;
			}
		}
		return true;		
	};
	
	public static Function<String, String> trimPontuation = s -> {
		int start = 0;
		int i=0;
		for (; i<s.length(); i++)
		{
			if (Character.isLetterOrDigit((s.charAt(i))))
			{
				start = i;
				break;
			}
		}		
		
		int end = s.length();
		if (i < end) {
			for (int j=end-1; j>i; j--)
			{
				if (Character.isLetterOrDigit((s.charAt(j))))
				{
					end = j+1;
					break;
				}
			}				
		}
		
		return s.substring(start, end);
	};
	
	public static BiPredicate<String, Integer> isBlankPadded = (s, pads) -> {
		if (pads + 1 > s.length())
			return false;
		if (' ' == s.charAt(pads))
			return false;
		for (int i = 0; i < pads; i++) {
			if (' ' != s.charAt(i))
				return false;
		}
		return true;				
	};
}
