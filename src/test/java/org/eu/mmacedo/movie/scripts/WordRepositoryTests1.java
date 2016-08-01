package org.eu.mmacedo.movie.scripts;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.eu.mmacedo.movie.scripts.domain.Word;
import org.eu.mmacedo.movie.scripts.repository.WordRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = { ApplicationTest1.class })
public class WordRepositoryTests1 {

	@Autowired
	private WordRepository wordRepository;

	@Test
	public void test1() {
		final List<Word> findBySettingByMovieCharacterByword = wordRepository
				.findBySettingByMovieCharacterByword("REBEL BLOCKADE RUNNER", "THREEPIO", "a");
		assertNotNull(findBySettingByMovieCharacterByword);
	}

}
