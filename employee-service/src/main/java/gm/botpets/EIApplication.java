package gm.botpets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
/*
 * Author:Shivakiran
 * */

@ComponentScan
@SpringBootApplication
public class EIApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(EIApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(EIApplication.class);
	}
}
