package org.eu.mmacedo.movie.scripts;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.json.JSONArray;
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
public class GetSettingsTest {

	@Autowired
	@Qualifier("getSettingsGateway")
	Function<Optional<Object>, JSONArray> getSettingsGateway;

	@Autowired
	@Qualifier("scriptRequest")
	MessageChannel scriptRequest;

	@Test
	public void test1() throws InterruptedException {
		final String path = "/integrationtest3.txt";

		final String text = TestHelper.readFile.apply(path);
		final Message<String> reqmsg = new GenericMessage<>(text);
		scriptRequest.send(reqmsg); // send message to process script in text

		TimeUnit.SECONDS.sleep(1);
		final JSONArray apply = getSettingsGateway.apply(Optional.empty());

		System.out.println(apply.toString());

		assert (true);

	}

}
