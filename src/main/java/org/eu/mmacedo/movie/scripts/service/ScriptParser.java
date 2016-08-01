package org.eu.mmacedo.movie.scripts.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.Function;

import org.eu.mmacedo.movie.scripts.Helper;
import org.eu.mmacedo.movie.scripts.domain.MovieCharacter;
import org.eu.mmacedo.movie.scripts.domain.Setting;
import org.eu.mmacedo.movie.scripts.domain.Word;
import org.springframework.beans.factory.annotation.Value;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScriptParser implements Function<Setting, Setting> {

	@Value("${character.blanks}")
	public Integer characterBlanks;

	@Value("${dialog.blanks}")
	public Integer dialogBlanks;

	@Override
	public Setting apply(final Setting t) {
		boolean searching = true;
		MovieCharacter character = null;

		if (log.isDebugEnabled()) {
			log.debug(t.getName());
		}

		for (final String line : t.getRaw()) {
			final String trimmed = line.trim();
			if (searching) {
				// test if Character
				if (Helper.isBlankPadded.test(line, characterBlanks) && Helper.isAllUpperCase.test(trimmed)) {
					character = t.getCharMap().get(trimmed);
					if (character == null) {
						character = new MovieCharacter();
						character.setName(trimmed);
						character.setIndex(new HashMap<>());
						character.setWords(new LinkedList<>());
						character.setSetting(t);
						t.getCharMap().put(trimmed, character);
						t.getCharacter().add(character);
					}
					searching = false;
				} else {
					continue; // next line
				}
			} else if (Helper.isBlankPadded.test(line, characterBlanks) && Helper.isAllUpperCase.test(trimmed)) {
				character = t.getCharMap().get(trimmed);
				if (character == null) {
					character = new MovieCharacter();
					character.setName(trimmed);
					character.setIndex(new HashMap<>());
					character.setWords(new LinkedList<>());
					character.setSetting(t);
					t.getCharMap().put(trimmed, character);
					t.getCharacter().add(character);
				}
			} else
			// test if dialog
			if (Helper.isBlankPadded.test(line, dialogBlanks)) {
				final String[] words = trimmed.split("\\s+");
				for (final String string : words) {
					final String w = Helper.trimPontuation.apply(string).toLowerCase();
					Word word = character.getIndex().get(w);
					if (word == null) {
						word = new Word();
						word.setCounter(0L);
						word.setWord(w);
						word.setCharacter(character);
						word.setSetting(t.getName());
						character.getIndex().put(w, word);
					}
					final Long counter = word.getCounter();
					word.setCounter(counter + 1);
					if (log.isDebugEnabled()) {
						if (counter > 0) {
							log.debug("Character(" + character.getName() + ") - Update word[" + word.getWord()
									+ "] Counter: " + word.getCounter());
						}
					}
				}
			}
		}

		return t;
	}

}
