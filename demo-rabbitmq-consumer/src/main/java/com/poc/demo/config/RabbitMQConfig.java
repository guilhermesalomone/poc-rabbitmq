package com.poc.demo.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarable;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * @author Guilherme.Salomone
 *
 */
@Configuration
public class RabbitMQConfig {

	public static final String EXCHANGE_NAME = "demo-rabbit-exchange";

	public static final String QUEUE_NAME = "demo-rabbit-queue";

	public static final String ROUTING_KEY = "demo.#";
	
	private static final String FANOUT = "fanout.demo";
	
	
    @Bean
    @ConditionalOnSingleCandidate(ConnectionFactory.class)
    @ConditionalOnMissingBean(RabbitTemplate.class)
	public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
	    final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
	    rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
	    return rabbitTemplate;
	}

	
	@Bean
	public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
	    return new Jackson2JsonMessageConverter();
	}
	
	@Bean
	public List<Declarable> fanoutBindings() {
	    Queue fanoutQueueRequest = new Queue(QUEUE_NAME, false, true, true);
	    FanoutExchange fanoutRequest = new FanoutExchange(FANOUT);
	 
	    return Arrays.asList(
	      fanoutQueueRequest,
	      BindingBuilder.bind(fanoutQueueRequest).to(fanoutRequest));
	}

	@Bean
	public List<Declarable> topicBindings() {
	    Queue topicQueueRequest = new Queue(QUEUE_NAME, false, true, true);
	 
	    TopicExchange topicExchange= new TopicExchange(EXCHANGE_NAME);
	 
	    return Arrays.asList(
	      topicQueueRequest,
	      topicExchange,
	      BindingBuilder.bind(topicQueueRequest).to(topicExchange).with(ROUTING_KEY));
	}
	
}
