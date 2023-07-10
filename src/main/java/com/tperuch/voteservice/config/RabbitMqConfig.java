package com.tperuch.voteservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class RabbitMqConfig {
    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.port}")
    private Integer port;
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;
    @Value("${spring.rabbitmq.exchange}")
    private String exchange;
    @Value("${spring.rabbitmq.session-status.queue}")
    private String sessionStatusqueue;
    @Value("${spring.rabbitmq.session-result.queue}")
    private String sessionResultQueue;

    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(exchange);
    }

    @Bean
    public Queue sessionStatusQueue(){
        return new Queue(sessionStatusqueue, true, false, false);
    }

    @Bean
    public Binding bindingExchangeToSessionStatusQueue(){
        return BindingBuilder.bind(sessionStatusQueue()).to(topicExchange()).with(sessionStatusqueue);
    }

    @Bean
    public Queue sessionResultQueue(){
        return new Queue(sessionResultQueue, true, false, false);
    }

    @Bean
    public Binding bindingExchangeToVoteSessionResultQueue(){
        return BindingBuilder.bind(sessionResultQueue()).to(topicExchange()).with(sessionResultQueue);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(){
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory());
        rabbitAdmin.declareExchange(topicExchange());
        rabbitAdmin.declareQueue(sessionStatusQueue());
        rabbitAdmin.declareQueue(sessionResultQueue());
        rabbitAdmin.declareBinding(bindingExchangeToSessionStatusQueue());
        rabbitAdmin.declareBinding(bindingExchangeToVoteSessionResultQueue());
        return rabbitAdmin;
    }
}
