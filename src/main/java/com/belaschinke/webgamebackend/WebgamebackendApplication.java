package com.belaschinke.webgamebackend;

import com.belaschinke.webgamebackend.service.WebgameService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class WebgamebackendApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(WebgamebackendApplication.class, args);
		WebgameService webgameService = context.getBean(WebgameService.class);

		System.out.println("Hello World!");

	}

}
