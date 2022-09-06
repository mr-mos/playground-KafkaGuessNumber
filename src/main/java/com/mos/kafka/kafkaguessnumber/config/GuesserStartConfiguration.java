package com.mos.kafka.kafkaguessnumber.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import java.util.UUID;

@Profile("numberGuesser")
@Configuration
public class GuesserStartConfiguration {


	@Bean
	public ReplyingKafkaTemplate<String, String, String> replyKafkaTemplate(ProducerFactory<String, String> pf, ConcurrentKafkaListenerContainerFactory<String, String> containerFactory) {
		String groupId = UUID.randomUUID().toString();
		containerFactory.setReplyTemplate(kafkaTemplate(pf));
		ConcurrentMessageListenerContainer<String, String> container = containerFactory.createContainer(GlobalDefs.TOPIC_FEEDBACK_NUMBER);
		container.getContainerProperties().setGroupId(groupId);
		ReplyingKafkaTemplate<String, String, String> replyer = new ReplyingKafkaTemplate<>(pf, container);
		replyer.setSharedReplyTopic(true);
		return replyer;
	}

	private KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> pf) {
		return new KafkaTemplate<>(pf);
	}
}
