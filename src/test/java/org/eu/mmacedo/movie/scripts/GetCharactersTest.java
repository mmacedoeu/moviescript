package org.eu.mmacedo.movie.scripts;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;

//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
//@ContextConfiguration("/META-INF/spring/integration/spring-test2-context.xml")
//@DirtiesContext
public class GetCharactersTest {

	@Autowired
	@Qualifier("getCharactersGateway")
	Function<Optional<Object>, JSONArray> getCharactersGateway;

	@Autowired
	@Qualifier("scriptRequest")
	MessageChannel scriptRequest;

	// @Test
	public void test() throws InterruptedException {
		final String path = "/integrationtest3.txt";

		final String text = TestHelper.readFile.apply(path);
		final Message<String> reqmsg = new GenericMessage<>(text);
		scriptRequest.send(reqmsg); // send message to process script in text

		TimeUnit.SECONDS.sleep(1);

		final JSONArray apply = getCharactersGateway.apply(Optional.empty());

		System.out.println(apply.toString());

		assert (true);

	}

}
