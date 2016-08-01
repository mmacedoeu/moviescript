package org.eu.mmacedo.movie.scripts;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({ "classpath:META-INF/spring/integration/hibernate-context.xml",
		"classpath:META-INF/spring/integration/spring-setting-context.xml" })
public class ApplicationTest2 {

}
