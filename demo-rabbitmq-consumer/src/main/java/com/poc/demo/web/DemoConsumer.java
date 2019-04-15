package com.poc.demo.web;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.json.JsonParseException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.poc.demo.config.RabbitMQConfig;
import com.poc.demo.domain.Data;

/**
 * 
 * @author Guilherme.Salomone
 *
 */
@Component
public class DemoConsumer {

	private static final Logger log = LoggerFactory.getLogger(DemoConsumer.class);
	
	private CountDownLatch latch = new CountDownLatch(1);

	@RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
	public void request(final Data message) throws JsonParseException, JsonMappingException, IOException {

		log.info("Received Request < {} >", message);
		
		latch.countDown();
	}

	public CountDownLatch getLatch() {
		return latch;
	}
}
