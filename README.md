# Introduction

Decoupling of software components is one of the most important parts of software design. 
One way of achieving this is using messaging systems, which provide an asynchronous way of communication between components (services). In this demo, we will cover one of such systems: RabbitMQ.

RabbitMQ is a message broker that implements Advanced Message Queuing Protocol (AMQP). It provides client libraries for major programming languages.

Besides using for decoupling software components RabbitMQ can be used for:

> Performing background operations
> Performing asynchronous operation

## Messaging Model

First, let’s have a quick, high-level look at how messaging works.

Simply put, there are two kinds of applications interacting with a messaging system: producers and consumers. Producers are those, who sends (publishes) messages to a broker, and consumers, who receive messages from the broker. Usually, this programs (software components) are running on different machines and RabbitMQ acts as a communication middleware between them.

In this article, we will discuss a simple example with two services which will communicate using RabbitMQ. One of the services will publish messages to RabbitMQ and the other one will consume.

## Setup


pull image and run rabbitmq server, more details at: https://hub.docker.com/_/rabbitmq

For the beginning let’s run RabbitMQ using official setup guide here.

We’ll naturally use the Java client for interacting with RabbitMQ server; the Maven dependency for this client is:

```sh

<dependency>
    <groupId>com.rabbitmq</groupId>
    <artifactId>amqp-client</artifactId>
    <version>4.0.0</version>
</dependency>

```


After running the RabbitMQ broker using the official guide, we need to connect to it using java client:
We can set username and the password

```sh

spring:
    rabbitmq:
        host: localhost
        port: 5672
        username: guest
        password: guest
  

```
We can use setPort to set the port if the default port is not used by the RabbitMQ Server; the default port for RabbitMQ is 15672:

Further, we will use this connection for publishing and consuming messages.

## Producer

Consider a simple scenario where a web application allows users to add new products to a website. Any time when new product added, we need to send an email to customers.

First, let’s define a queue:

```sh
private static final String ROUTING_KEY = "demo.";

rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, ROUTING_KEY + this.hashCode(), new Data(msg));

```

This message will be consumed by another service, which is responsible for sending emails to customers.

## Consumer

Let’s see what we can implement the consumer side; we’re going to declare the same queue:

Here’s how we define the consumer that will process messages from queue asynchronously:

```sh

	@RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
	public void request(final Data message) throws JsonParseException, JsonMappingException, IOException {

		log.info("Received Request < {} >", message);
		
		latch.countDown();
	}

```

# Conclusion

This simple article covered basic concepts of RabbitMQ and discussed a simple example using it.


### Reference

https://www.baeldung.com/rabbitmq-spring-amqp

https://thepracticaldeveloper.com/2016/10/23/produce-and-consume-json-messages-with-spring-boot-amqp/

https://www.baeldung.com/rabbitmq

https://reflectoring.io/event-messaging-with-spring-boot-and-rabbitmq/








