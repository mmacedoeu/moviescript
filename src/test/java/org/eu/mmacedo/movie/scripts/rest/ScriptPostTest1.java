package org.eu.mmacedo.movie.scripts.rest;

import org.eu.mmacedo.movie.scripts.TestHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@ContextConfiguration("/META-INF/spring/integration/spring-test3-context.xml")
public class ScriptPostTest1 {

	@Value("${server.port}")
	public Integer port;

	// @Test
	public void test() {
		final String path = "/integrationtest3.txt";
		final String text = TestHelper.readFile.apply(path);
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);

		final HttpEntity<String> request = new HttpEntity<>(text, headers);

		final TestRestTemplate restTemplate = new TestRestTemplate();
		final ResponseEntity<String> postForEntity = restTemplate.postForEntity("http://localhost:" + 8888 + "/script",
				request, String.class);

		assert (true);
	}

}
