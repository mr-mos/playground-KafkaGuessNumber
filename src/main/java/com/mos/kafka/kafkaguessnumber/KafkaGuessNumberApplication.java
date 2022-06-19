package com.mos.kafka.kafkaguessnumber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaGuessNumberApplication {

	private static final Logger log	= LoggerFactory.getLogger(KafkaGuessNumberApplication.class);


	public static void main(String[] args) {
		SpringApplication.run(KafkaGuessNumberApplication.class, args);
	}







}
