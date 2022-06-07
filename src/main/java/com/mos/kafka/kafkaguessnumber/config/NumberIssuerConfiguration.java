package com.mos.kafka.kafkaguessnumber.config;

import com.mos.kafka.kafkaguessnumber.logic.Issuer;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;

@Profile("numberIssuer")
@Configuration
public class NumberIssuerConfiguration {

	private final Issuer issuer;

	public NumberIssuerConfiguration(Issuer issuer) {
		this.issuer = issuer;
	}

	@Bean
	public NewTopic newNumberTopic() {
		return TopicBuilder.name("newNumberTopic")               // a timestamp
				.partitions(1)
				.replicas(1)
				.build();
	}

	@Bean
	public NewTopic guessNumberTopic() {
		return TopicBuilder.name("guessNumberTopic")              // timestamp;number
				.partitions(1)
				.replicas(1)
				.build();
	}


	@Bean
	public NewTopic feedbackNumberTopic() {
		return TopicBuilder.name("feedbackNumberTopic")            //  Line: "Matched" or "Inactive" or "<" or ">" or "="
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
