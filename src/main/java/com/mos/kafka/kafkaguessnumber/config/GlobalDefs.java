package com.mos.kafka.kafkaguessnumber.config;

public class GlobalDefs {

	/*
	   Broadcasting a new number-guess task to all. Task is identified by a timestamp
	   PAYLOAD:  unique-id  (using a timestamp for better logging)
	 */
	static public final String TOPIC_NEW_NUMBER = "newNumberTopic";

	/*
	   Guesser sends his number.  Used as a synchronous request-(reply) queue.
	   PAYLOAD:  unique-id;number
	 */
	static public final String TOPIC_GUESS_NUMBER = "guessNumberTopic";

	/*
	   Used as a synchronous (request)-reply queue. Guesser gets his feedback
	   PAYLOAD:  "Matched" or "Inactive" or "<" or ">"
	 */
	static public final String TOPIC_FEEDBACK_NUMBER = "feedbackNumberTopic";

}
