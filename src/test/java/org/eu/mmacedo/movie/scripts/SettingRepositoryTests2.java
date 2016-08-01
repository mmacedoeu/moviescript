package org.eu.mmacedo.movie.scripts;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.util.Optional;

import org.eu.mmacedo.movie.scripts.domain.Setting;
import org.eu.mmacedo.movie.scripts.repository.SettingRepository;
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
@DatabaseSetup(SettingRepositoryTests2.DATASET)
@DirtiesContext
public class SettingRepositoryTests2 {

	protected static final String DATASET = "classpath:datasets/set-items1.xml";

	@Autowired
	private SettingRepository settingRepository;

	@Test
	public void test1() {
		final Optional<Setting> findByName = settingRepository.findByName("REBEL BLOCKADE RUNNER");
		assertNotNull(findByName);
		assertThat(findByName.isPresent());
		assertThat("REBEL BLOCKADE RUNNER".equals(findByName.get().getName()));
	}

}
