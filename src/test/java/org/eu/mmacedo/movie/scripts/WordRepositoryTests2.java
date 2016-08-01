package org.eu.mmacedo.movie.scripts;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.eu.mmacedo.movie.scripts.domain.Word;
import org.eu.mmacedo.movie.scripts.repository.WordRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = { ApplicationTest2.class })
@DatabaseSetup(WordRepositoryTests2.DATASET)
@DirtiesContext
public class WordRepositoryTests2 {

	protected static final String DATASET = "classpath:datasets/word-items1.xml";

	@Autowired
	private WordRepository wordRepository;

	@Test
	public void test1() {
		final List<Word> findBySettingByMovieCharacterByword = wordRepository
				.findBySettingByMovieCharacterByword("TATOOINE", "THREEPIO", "a");
		assertNotNull(findBySettingByMovieCharacterByword);
		assert (!findBySettingByMovieCharacterByword.isEmpty());
		final Word next = findBySettingByMovieCharacterByword.get(0);
		assert ("TATOOINE".equals(next.getCharacter().getSetting().getName()));
		assert ("THREEPIO".equals(next.getCharacter().getName()));
		assert ("a".equals(next.getWord()));
		assertThat(next.getCounter() == 1L);
	}

}
