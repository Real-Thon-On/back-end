package com.fizz.fizz_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
@EnableScheduling // 스케줄러 활성화
@SpringBootApplication
public class FizzApplication {

	public static void main(String[] args) {
		SpringApplication.run(FizzApplication.class, args);
	}

}
