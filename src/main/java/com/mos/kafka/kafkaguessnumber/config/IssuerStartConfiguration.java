package com.mos.kafka.kafkaguessnumber.config;

import com.mos.kafka.kafkaguessnumber.logic.Issuer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.concurrent.TimeUnit;

@Profile("numberIssuer")
@Configuration
public class IssuerStartConfiguration {

	private static final Logger log	= LoggerFactory.getLogger(IssuerStartConfiguration.class);


	private final Issuer issuer;

	public IssuerStartConfiguration(Issuer issuer) {
		this.issuer = issuer;
	}


	@Bean
	public ApplicationRunner runner() {
		return args -> {
			log.info("***** Waiting 10 seconds for consumers and then start the game.... ****");
			TimeUnit.SECONDS.sleep(10);
			issuer.publishNewNumberEvent();
		};
	}

}
