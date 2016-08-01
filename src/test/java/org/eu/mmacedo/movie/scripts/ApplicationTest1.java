package org.eu.mmacedo.movie.scripts;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({ "classpath:META-INF/spring/integration/hibernate-context.xml" })
public class ApplicationTest1 {
	public static void main(final String[] args) throws Exception {
		final ConfigurableApplicationContext ctx = SpringApplication.run(ApplicationTest1.class, args);
	}
}
