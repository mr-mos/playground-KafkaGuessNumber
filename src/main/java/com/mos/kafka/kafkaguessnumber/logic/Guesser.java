package com.mos.kafka.kafkaguessnumber.logic;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.mos.kafka.kafkaguessnumber.config.GlobalDefs.*;

@Profile("numberGuesser")
@Component
public class Guesser {

	private final Random rand = new Random();

	public static final String guesserId = "guesser-" + UUID.randomUUID().toString().substring(0, 8);

	private static final Logger log = LoggerFactory.getLogger(Guesser.class);

	private volatile String currentChallenge = "";

	private volatile Integer currentGuess;

	private volatile Integer lastSmallerGuess = MAX_NUMBER;

	private volatile Integer lastGreaterGuess = 0;

	private final ExecutorService executorService = Executors.newFixedThreadPool(5);


	private final ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;


	public Guesser(ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate) {
		this.replyingKafkaTemplate = replyingKafkaTemplate;
	}


	@KafkaListener(id = "#{T(com.mos.kafka.kafkaguessnumber.logic.Guesser).guesserId}", topics = TOPIC_NEW_NUMBER)
	public void listenToNewNumberChallenge(String challengeId, @Header(KafkaHeaders.CONSUMER) KafkaConsumer<?,?> kafkaConsumer) {
		log.info(String.format("<--------received----------\n  " +
				"Consumer %s got number challenge with id: %s", guesserId, challengeId));
		currentChallenge = challengeId;
		currentGuess = null;
		lastSmallerGuess = null;
		lastGreaterGuess = null;
		executorService.submit(() -> startGuessing(challengeId));
	}


	private void startGuessing(String challenge) {
		thinkingTime();
		if (currentChallenge.equals(challenge)) {
			log.debug("Starting challenge " + challenge);
			sendNextGuess(null);
		}
	}


	private void sendNextGuess(String lastHint) {
		currentGuess = calcNextGuess(lastHint);
		log.info(String.format("--------sending---------->\n  " +
				"New guess with sending number: %s", currentGuess));
		String payload = currentChallenge+";"+currentGuess;
		ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_GUESS_NUMBER, payload);
		RequestReplyFuture<String, String, String> future =	replyingKafkaTemplate.sendAndReceive(record);
		try {
			ConsumerRecord<String, String> response = future.get(10, TimeUnit.SECONDS);
			log.info(String.format("<--------received----------\n  " +
					"Answer from Issuer regarding number %s: %s", currentGuess, response.value()));
			// check the answer
		} catch (Exception e) {
			log.error("Did not get feedback for guess " + currentGuess, e);
		}
	}


	private Integer calcNextGuess(String lastHint) {
		if (lastHint == null || currentGuess == null) {
			return rand.nextInt(MAX_NUMBER-(MAX_NUMBER/10) + (MAX_NUMBER/10));         // first try: randomly to give guesser different start points
		}
		if (lastHint.equals(SMALLER)) {
			return (currentGuess - lastGreaterGuess) / 2;
		} else {
			return (lastSmallerGuess - currentGuess) / 2;
		}
	}


	private void thinkingTime() {
		try {
			TimeUnit.SECONDS.sleep(1);    // thinking time ;)
		} catch (InterruptedException e) {
			log.error("Can't sleep. To much coffein?", e);
		}
	}

}
