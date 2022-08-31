package com.mos.kafka.kafkaguessnumber.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;

import static com.mos.kafka.kafkaguessnumber.config.GlobalDefs.*;

@Profile({"numberIssuer","numberGuesser"})
@Configuration
public class GuessNumberConfiguration {


	@Bean
	public NewTopic newNumberTopic() {
		return TopicBuilder.name(TOPIC_NEW_NUMBER)
				.partitions(1)
				.replicas(1)
				.build();
	}

	@Bean
	public NewTopic guessNumberTopic() {
		return TopicBuilder.name(TOPIC_GUESS_NUMBER)
				.partitions(1)
				.replicas(1)
				.build();
	}


	@Bean
	public NewTopic feedbackNumberTopic() {
		return TopicBuilder.name(TOPIC_FEEDBACK_NUMBER)
				.partitions(1)
				.replicas(1)
				.build();
	}


}
