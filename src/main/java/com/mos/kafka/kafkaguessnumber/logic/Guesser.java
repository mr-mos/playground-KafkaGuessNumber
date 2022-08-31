package com.mos.kafka.kafkaguessnumber.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("numberGuesser")
@Component
public class Guesser {

	private static final Logger log = LoggerFactory.getLogger(Guesser.class);




}
