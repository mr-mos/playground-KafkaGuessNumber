package com.mos.kafka.kafkaguessnumber.logic;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.mos.kafka.kafkaguessnumber.config.GlobalDefs.TOPIC_NEW_NUMBER;

@Profile("numberGuesser")
@Component
public class Guesser {

	public static final String guesserId = "guesser-"+UUID.randomUUID().toString().substring(0,8);

	private static final Logger log = LoggerFactory.getLogger(Guesser.class);

	private volatile String currentChallenge = "";

	private ExecutorService executorService = Executors.newFixedThreadPool(5);


	@KafkaListener(id = "#{T(com.mos.kafka.kafkaguessnumber.logic.Guesser).guesserId}", topics = TOPIC_NEW_NUMBER)
	public void listenToNewNumberChallenge(String challengeId, @Header(KafkaHeaders.CONSUMER) KafkaConsumer kafkaConsumer) {
		log.info(String.format("<--------received----------\n  " +
				"Consumer %s got number challenge with id: %s", guesserId, challengeId));
		currentChallenge = challengeId;
		executorService.submit(() -> startGuessing(challengeId));
	}


	private void startGuessing(String challenge)  {
		thinkingTime();
		if (currentChallenge.equals(challenge)) {
			log.info("Lets DOOOOOOOOOOOOOOOO the work: " + challenge);
		}
	}

	private void thinkingTime() {
		try {
			Thread.sleep(3000);    // thinking time ;)
		} catch (InterruptedException e) {
			log.error("Can't sleep. To much coffein?", e);
		}
	}

}
