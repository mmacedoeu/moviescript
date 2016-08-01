package org.eu.mmacedo.movie.scripts;

import static org.junit.Assert.assertNotNull;

import java.util.Optional;

import org.eu.mmacedo.movie.scripts.domain.Setting;
import org.eu.mmacedo.movie.scripts.repository.SettingRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = { ApplicationTest1.class })
public class SettingRepositoryTest1 {

	@Autowired
	private SettingRepository settingRepository;

	@Test
	public void test1() {
		final Optional<Setting> findByName = settingRepository.findByName("REBEL BLOCKADE RUNNER");
		assertNotNull(findByName);
		assert (!findByName.isPresent());
	}

}
