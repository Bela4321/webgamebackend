package com.belaschinke.webgamebackend;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class WebgamebackendApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(WebgamebackendApplication.class, args);
		//get beans
		ConfigurableListableBeanFactory bf= context.getBeanFactory();

		System.out.println("Hello World!");

	}

}
