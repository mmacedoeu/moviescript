package org.eu.mmacedo.movie.scripts;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.eu.mmacedo.movie.scripts.domain.MovieCharacter;
import org.eu.mmacedo.movie.scripts.repository.MovieCharacterRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = { ApplicationTest1.class })
public class CharacterRepositoryTest1 {

	@Autowired
	private MovieCharacterRepository charRepository;

	@Test
	public void test1() {
		final List<MovieCharacter> findByName = charRepository.findRootByName("THREEPIO");
		assertNotNull(findByName);
		assertThat(findByName.isEmpty());
	}

}
