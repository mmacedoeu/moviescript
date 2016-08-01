package org.eu.mmacedo.movie.scripts;

import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.eu.mmacedo.movie.scripts.domain.MovieCharacter;
import org.eu.mmacedo.movie.scripts.repository.MovieCharacterRepository;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration("/META-INF/spring/integration/spring-test2-context.xml")
public class GetCharacterTest {

	@Autowired
	@Qualifier("getCharacterGateway")
	Function<Long, Optional<JSONObject>> getCharacterGateway;

	@Autowired
	@Qualifier("scriptRequest")
	MessageChannel scriptRequest;

	@Autowired
	private MovieCharacterRepository charRepository;

	@Test
	public void test() throws InterruptedException {
		final String path = "/integrationtest3.txt";

		final String text = TestHelper.readFile.apply(path);
		final Message<String> reqmsg = new GenericMessage<>(text);
		scriptRequest.send(reqmsg); // send message to process script in text

		TimeUnit.SECONDS.sleep(1);

		final List<MovieCharacter> findAllRoot = charRepository.findAllRoot();
		assertNotNull(findAllRoot);
		assert (!findAllRoot.isEmpty());
		final MovieCharacter movieCharacter = findAllRoot.get(0);

		final Optional<JSONObject> apply = getCharacterGateway.apply(movieCharacter.getId());

		System.out.println(apply.toString());

		assert (apply.isPresent());
	}

}
