package com.mos.kafka.kafkaguessnumber.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class Issuer {

	private static final Logger log = LoggerFactory.getLogger(Issuer.class);

	private final KafkaTemplate<String, String> kafkaTemplate;

	private String timestamp;
	private Integer randomNumber;

	private Random rand = new Random();


	public Issuer(KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
		generateNewNumber();
	}


	public void generateNewNumber() {
		timestamp = System.currentTimeMillis() + "";
		randomNumber = rand.nextInt(100) + 1;            // number between 1 and 100
	}


	// consumers get notified about a new quiz
	public void publishNewNumberEvent() {
		kafkaTemplate.send("newNumberTopic", timestamp);
		log.info(String.format("----------------------------------------------->\n  " +
				"Publishing new number quiz. Timestamp:%s  Number:%d ", timestamp, randomNumber));
	}


	@KafkaListener(id = "theIssuer", topics = "guessNumberTopic")
	public void listenToGuesses(String timestampPlusGuess, ConsumerRecordMetadata meta) {
		log.info(String.format("Got number guess '%s' from '%s' ", timestampPlusGuess, meta.toString()));
		String[] splits = timestampPlusGuess.split(";");
		String guessTimestamp = splits[0];
		Integer guessNumber = Integer.valueOf(splits[1]);
		String answer;
		if (!guessTimestamp.equals(timestamp)) {
			answer = "Inactive";
		} else if (guessNumber.equals(randomNumber)) {
			answer = "Matched";
			generateNewNumber();
			publishNewNumberEvent();
		} else {
			answer = randomNumber.compareTo(guessNumber) > 0 ? ">" : "<";
		}
		log.info("Answer: " + answer);
	}


}
