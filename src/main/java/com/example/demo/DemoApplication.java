package com.example.demo;

import com.example.demo.service.FileWatcher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
@EnableCaching
public class DemoApplication {

	public static void main(String[] args){

		ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
		FileWatcher fileWatcher = context.getBean(FileWatcher.class);
		fileWatcher.watchFolder(args[0]);

	}

}
