package com.poc.demo.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.poc.demo.config.RabbitMQConfig;
import com.poc.demo.domain.Data;

/**
 * 
 * @author Guilherme.Salomone
 *
 */
@Component
public class DemoProducer {

	private static final Logger log = LoggerFactory.getLogger(DemoProducer.class);

	private static final String ROUTING_KEY = "demo.";
	
	private final RabbitTemplate rabbitTemplate;

	public DemoProducer(RabbitTemplate rabbitTemplate) {
		super();
		this.rabbitTemplate = rabbitTemplate;
	}

	public void sendMsgCuston(String msg) {

		log.debug("Sending Request to be Saved Queue: {} , Object: {}", RabbitMQConfig.QUEUE_NAME, msg);

		rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, ROUTING_KEY + this.hashCode(), new Data(msg));

	}
}
