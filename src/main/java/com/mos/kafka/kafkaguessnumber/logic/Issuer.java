package com.mos.kafka.kafkaguessnumber.logic;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.mos.kafka.kafkaguessnumber.config.GlobalDefs.*;

@Profile("numberIssuer")
@Component
public class Issuer {

	private static final Logger log = LoggerFactory.getLogger(Issuer.class);

	private final KafkaTemplate<String, String> kafkaTemplate;

	private boolean active = true;

	private String timestamp;

	private Set<String> clientIds;

	private String clientWinner;

	private Integer count;

	private Integer randomNumber;

	private Random rand = new Random();

	private final ExecutorService executorService = Executors.newSingleThreadExecutor();


	public Issuer(KafkaTemplate<String, String> kafkaTemplate, KafkaAdmin kafkaAdmin) {
		this.kafkaTemplate = kafkaTemplate;
		generateNewNumber();
	}


	public void generateNewNumber() {
		timestamp = System.currentTimeMillis() + "";
		randomNumber = rand.nextInt(MAX_NUMBER) + 1;            // number between 1 and MAX
		clientIds = new HashSet<>();
		clientWinner = null;
		count = 0;
		active = true;
	}


	// consumers get notified about a new quiz
	public void publishNewNumberEvent() {
		kafkaTemplate.send(TOPIC_NEW_NUMBER, timestamp);
		log.info(String.format("--------------------send---------------------->\n  " +
				"Publishing new number quiz. Timestamp:%s  Number:%d ", timestamp, randomNumber));
	}


	@KafkaListener(id = "theIssuer", topics = TOPIC_GUESS_NUMBER)
	@SendTo      // goes to TOPIC_FEEDBACK_NUMBER because of the ReplyingKafkaTemplate configuration
	public String listenToGuesses(ConsumerRecord<String, String> record, ConsumerRecordMetadata meta) {
		String timestampPlusGuess = record.value();
		log.info(String.format("<--------received:%s----------\n  " +
				"Got number guess '%s' from '%s'.  ", record.key(), timestampPlusGuess, meta.toString()));
		String[] splits = timestampPlusGuess.split(";");
		String guessTimestamp = splits[0];
		Integer guessNumber = Integer.valueOf(splits[1]);
		String answer;
		if (!active || !guessTimestamp.equals(timestamp)) {
			answer = NOT_ACTIVE;
			log.warn("Got guess from " + record.key() + " of inactive quiz: " + guessTimestamp);
		} else if (guessNumber.equals(randomNumber)) {
			answer = MATCHED;
			active = false;
			logHistory(record.key(), true);
			prepareNewQuiz();
		} else {
			logHistory(record.key(), false);
			answer = randomNumber.compareTo(guessNumber) > 0 ? GREATER : SMALLER;
		}
		log.info(String.format("--------send:%s-------------->\n  " +
						"Answering guess %d regarding wanted number (%d): %s ",
				record.key(), guessNumber, randomNumber, answer));
		return answer;
//		return MessageBuilder.withPayload(answer)
//				.setHeader(KafkaHeaders.CORRELATION_ID, record.headers().lastHeader(KafkaHeaders.CORRELATION_ID).value())
//				.setHeader(KafkaHeaders.TOPIC, record.headers().lastHeader(KafkaHeaders.REPLY_TOPIC).value())
//				.build();
	}

	private void logHistory(String clientId, boolean winner) {
		count++;
		clientIds.add(clientId);
		if (winner) clientWinner =clientId;
	}


	private void prepareNewQuiz() {
		executorService.submit(() -> {
			try {
				TimeUnit.SECONDS.sleep(2);
				long seconds = (System.currentTimeMillis() - Long.valueOf(this.timestamp)) / 1000;
				log.info("\n*********************************************************"
						+ "\nLast quiz for number "+this.randomNumber+" is resolved! Id:"+timestamp
						+ "\nTime: " + seconds + " sec."
						+ "\nGuesser: " + this.clientIds.size() + " (" + String.join(",", this.clientIds) + ")"
						+ "\nAttempts: " + count
						+ "\nWinner: " + clientWinner
						+ "\n********************************************************");
				TimeUnit.SECONDS.sleep(10);
			} catch (Exception e) {
				log.error("Can't sleep and log before starting new quiz.",e);
			}
			generateNewNumber();
			publishNewNumberEvent();
		});
	}

}
