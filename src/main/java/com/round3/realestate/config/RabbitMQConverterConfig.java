package com.round3.realestate.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConverterConfig {

    @Bean
    public Queue bidQueue(){
        return new Queue("bid.queue", true);
    }

    @Bean
    public DirectExchange bidExchange(){
        return new DirectExchange("bid.exchange", true, false);
    }

    @Bean
    public Binding bidBinding(Queue bidQueue, DirectExchange bidExchange){
        return BindingBuilder.bind(bidQueue).to(bidExchange).with("bid.routingkey");
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}