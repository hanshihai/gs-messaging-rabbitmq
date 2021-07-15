package com.example.messagingrabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingRabbitmqConfig {

    @Value("${spring.rabbitmq.topic}") private String topicExchangeName;
    @Value("${spring.rabbitmq.queue}") private String queueName;
    @Value("${spring.rabbitmq.routingKey}") private String routingKey;

    @Bean
    Queue queue() {
        return new Queue(queueName);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    @Bean
    public ConnectionFactory connectionFactory(
            @Value("${spring.rabbitmq.host}") String rabbitHost,
            @Value("${spring.rabbitmq.port}") int rabbitPort,
            @Value("${spring.rabbitmq.username}") String userName,
            @Value("${spring.rabbitmq.password}") String rabbitPassword,
            @Value("${spring.rabbitmq.virtualHost}") String virtualHost) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        this.populateConnectionFactoryAttributes(connectionFactory, rabbitHost, rabbitPort, userName, rabbitPassword, virtualHost);
        return connectionFactory;
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    private void populateConnectionFactoryAttributes (
            CachingConnectionFactory connectionFactory, String host, int port, String userName, String password, String virtualHost){
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(userName);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
    }

}
