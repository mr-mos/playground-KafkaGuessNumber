package com.mos.kafka.kafkaguessnumber.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;

@Profile("numberIssuer")
@Configuration
public class GuessNumberConfiguration {

	private static final Logger log	= LoggerFactory.getLogger(GuessNumberConfiguration.class);


	@Bean
	public NewTopic newNumberTopic() {
		return TopicBuilder.name("newNumberTopic")               // a timestamp
				.partitions(1)
				.replicas(1)
				.build();
	}

	@Bean
	public NewTopic guessNumberTopic() {
		return TopicBuilder.name("guessNumberTopic")               // number
				.partitions(1)
				.replicas(1)
				.build();
	}


	@Bean
	public NewTopic feedbackNumberTopic() {
		return TopicBuilder.name("feedbackNumberTopic")            // message:  1.) Line: Timestamp   2.) Line: <guessedNumber>  "<" or ">" or "="
				.partitions(1)
				.replicas(1)
				.build();
	}



}
