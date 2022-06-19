package com.mos.kafka.kafkaguessnumber.config;

import com.mos.kafka.kafkaguessnumber.logic.Issuer;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;

import static com.mos.kafka.kafkaguessnumber.config.GlobalDefs.*;

@Profile("numberIssuer")
@Configuration
public class NumberIssuerConfiguration {


	private final Issuer issuer;

	public NumberIssuerConfiguration(Issuer issuer) {
		this.issuer = issuer;
	}

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

	@Bean
	public ApplicationRunner runner() {
		return args -> {
			issuer.publishNewNumberEvent();
		};
	}

}
