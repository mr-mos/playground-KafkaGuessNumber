package com.mos.kafka.kafkaguessnumber;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class KafkaDemoApplication {

	private static final Logger log	= LoggerFactory.getLogger(KafkaDemoApplication.class);


	public static void main(String[] args) {
		SpringApplication.run(KafkaDemoApplication.class, args);
	}

	@Bean
	public NewTopic topic() {
		return TopicBuilder.name("demoTopic")
				.partitions(1)
				.replicas(1)
				.build();
	}

	@KafkaListener(id = "demoConsumer", topics = "demoTopic")
	public void listen(String in) {
		log.info("--------------------> "+in);
	}

	@Bean
	public ApplicationRunner runner(KafkaTemplate<String, String> template) {
		return args -> {
			for (int i = 0; i < 5; i++) {
				template.send("demoTopic", "My Kafka Event Nr."+(i+1));
				TimeUnit.SECONDS.sleep(5);
			}
		};
	}


}
