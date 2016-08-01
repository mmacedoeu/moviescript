package org.eu.mmacedo.movie.scripts;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

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
public class GetSettingTest {

	@Autowired
	@Qualifier("getSettingGateway")
	Function<Long, Optional<JSONObject>> getSettingGateway;

	@Autowired
	@Qualifier("scriptRequest")
	MessageChannel scriptRequest;

	@Test
	public void test() throws InterruptedException {
		final String path = "/integrationtest3.txt";

		final String text = TestHelper.readFile.apply(path);
		final Message<String> reqmsg = new GenericMessage<>(text);
		scriptRequest.send(reqmsg); // send message to process script in text

		TimeUnit.SECONDS.sleep(1);
		final Optional<JSONObject> apply = getSettingGateway.apply(1L);

		System.out.println(apply.toString());

		assert (apply.isPresent());
	}

}
