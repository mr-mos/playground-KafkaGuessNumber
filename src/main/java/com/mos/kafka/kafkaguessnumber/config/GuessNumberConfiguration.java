package com.mos.kafka.kafkaguessnumber.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

import static com.mos.kafka.kafkaguessnumber.config.GlobalDefs.*;

@Profile({"numberIssuer","numberGuesser"})
@Configuration
public class GuessNumberConfiguration {


	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapAddress;


	@Bean
	public KafkaAdmin kafkaAdmin() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		return new KafkaAdmin(configs);
	}



	@Bean
	public NewTopic newNumberTopic() {
		return TopicBuilder.name(TOPIC_NEW_NUMBER)
				.partitions(1)
				.replicas(1)
				.config(TopicConfig.RETENTION_MS_CONFIG, "1500")
				.config(TopicConfig.SEGMENT_MS_CONFIG, "1000")
				.build();
	}

	@Bean
	public NewTopic guessNumberTopic() {
		return TopicBuilder.name(TOPIC_GUESS_NUMBER)
				.partitions(1)
				.replicas(1)
				.config(TopicConfig.RETENTION_MS_CONFIG, "1500")
				.config(TopicConfig.SEGMENT_MS_CONFIG, "1000")
				.build();
	}


	@Bean
	public NewTopic feedbackNumberTopic() {
		return TopicBuilder.name(TOPIC_FEEDBACK_NUMBER)
				.partitions(1)
				.replicas(1)
				.config(TopicConfig.RETENTION_MS_CONFIG, "1500")
				.config(TopicConfig.SEGMENT_MS_CONFIG, "1000")
				.build();
	}


}
