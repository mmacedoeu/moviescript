package org.eu.mmacedo.movie.scripts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({ "classpath:META-INF/spring/integration/spring-integration-context.xml" })
public class Application {

	public static void main(final String[] args) throws Exception {
		final ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
		System.out.println("Hit Enter to terminate");
		System.in.read();

		ctx.close();
	}
}
