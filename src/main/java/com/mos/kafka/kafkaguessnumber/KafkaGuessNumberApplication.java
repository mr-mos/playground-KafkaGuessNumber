package com.mos.kafka.kafkaguessnumber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class KafkaGuessNumberApplication {

	private static final Logger log	= LoggerFactory.getLogger(KafkaGuessNumberApplication.class);


	public static void main(String[] args) {
		SpringApplication.run(KafkaGuessNumberApplication.class, args);
	}





	@Bean
	public ApplicationRunner runner(KafkaTemplate<String, String> template) {
		return args -> {
			for (int i = 0; i < 5; i++) {
				template.send("demoTopic", "My Kafka Event Nr."+(i+1));
				log.debug("Sending event to demoTopic Nr. "+(i+1));
				TimeUnit.SECONDS.sleep(5);
			}
		};
	}


}
