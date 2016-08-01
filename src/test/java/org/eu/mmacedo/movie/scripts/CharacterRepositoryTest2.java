package org.eu.mmacedo.movie.scripts;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.eu.mmacedo.movie.scripts.domain.MovieCharacter;
import org.eu.mmacedo.movie.scripts.repository.MovieCharacterRepository;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = { ApplicationTest1.class })
@DatabaseSetup(CharacterRepositoryTest2.DATASET)
@DirtiesContext
public class CharacterRepositoryTest2 {

	protected static final String DATASET = "classpath:datasets/char-items1.xml";

	@Autowired
	private MovieCharacterRepository charRepository;

	@Test
	public void test1() {
		final List<MovieCharacter> findByName = charRepository.findRootByName("THREEPIO");
		assertNotNull(findByName);
		assert (findByName.size() == 1);
		assert ("THREEPIO".equals(findByName.get(0).getName()));
	}
}
