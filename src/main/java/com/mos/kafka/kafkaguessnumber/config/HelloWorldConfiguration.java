package com.mos.kafka.kafkaguessnumber.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.TimeUnit;

@Profile("helloWorld")
@Configuration
public class HelloWorldConfiguration {

	private static final Logger log	= LoggerFactory.getLogger(HelloWorldConfiguration.class);

	@Bean
	public NewTopic demoTopic() {
		return TopicBuilder.name("demoTopic")
				.partitions(1)
				.replicas(1)
				.config(TopicConfig.RETENTION_MS_CONFIG, "1500")
				.config(TopicConfig.SEGMENT_MS_CONFIG, "1000")
				.build();
	}

	@KafkaListener(id = "#{T(java.util.UUID).randomUUID().toString()}", topics = "demoTopic")
	public void listen(String in) {
		log.info("------- Received -------------> "+in);
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
