package com.mos.kafka.kafkaguessnumber.config;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class BeanConfiguration {

	private static final Logger log	= LoggerFactory.getLogger(BeanConfiguration.class);


	@Bean
	public NewTopic demoTopic() {
		return TopicBuilder.name("demoTopic")
				.partitions(1)
				.replicas(1)
				.config(TopicConfig.RETENTION_MS_CONFIG, "1000")
				.config(TopicConfig.SEGMENT_MS_CONFIG, "1000")
				.build();
	}

	@Bean
	public NewTopic guessNumberTopic() {
		return TopicBuilder.name("guessNumberTopic")
				.partitions(1)
				.replicas(1)
				.build();
	}


	@Bean
	public NewTopic feedbackNumberTopic() {
		return TopicBuilder.name("feedbackNumberTopic")
				.partitions(1)
				.replicas(1)
				.build();
	}


	@KafkaListener(id = "#{T(java.util.UUID).randomUUID().toString()}", topics = "demoTopic")
	public void listen(String in) {
		log.info("--------------------> "+in);
	}
}
