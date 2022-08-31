package com.mos.kafka.kafkaguessnumber.config;

import com.mos.kafka.kafkaguessnumber.logic.Issuer;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("numberIssuer")
@Configuration
public class IssuerStartConfiguration {


	private final Issuer issuer;

	public IssuerStartConfiguration(Issuer issuer) {
		this.issuer = issuer;
	}


	@Bean
	public ApplicationRunner runner() {
		return args -> {
			issuer.publishNewNumberEvent();
		};
	}

}
