package com.hospital.schedulingservice.infra.messaging.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String APPOINTMENT_EXCHANGE = "hospital.appointments";
    public static final String APPOINTMENT_CREATED_QUEUE = "hospital.appointments.created";
    public static final String APPOINTMENT_CREATED_ROUTING_KEY = "appointment.created";
    public static final String APPOINTMENT_UPDATED_QUEUE = "hospital.appointments.updated";
    public static final String APPOINTMENT_UPDATED_ROUTING_KEY = "appointment.updated";

    @Bean
    TopicExchange appointmentExchange() {
        return new TopicExchange(APPOINTMENT_EXCHANGE, true, false);
    }

    @Bean
    Queue appointmentCreatedQueue() {
        return new Queue(APPOINTMENT_CREATED_QUEUE, true);
    }

    @Bean
    Queue appointmentUpdatedQueue() {
        return new Queue(APPOINTMENT_UPDATED_QUEUE, true);
    }

    @Bean
    Binding appointmentCreatedBinding(
            @Qualifier("appointmentCreatedQueue") Queue appointmentCreatedQueue,
            TopicExchange appointmentExchange) {

        return BindingBuilder
                .bind(appointmentCreatedQueue)
                .to(appointmentExchange)
                .with(APPOINTMENT_CREATED_ROUTING_KEY);
    }

    @Bean
    Binding appointmentUpdatedBinding(
            @Qualifier("appointmentUpdatedQueue") Queue appointmentUpdatedQueue,
            TopicExchange appointmentExchange) {

        return BindingBuilder
                .bind(appointmentUpdatedQueue)
                .to(appointmentExchange)
                .with(APPOINTMENT_UPDATED_ROUTING_KEY);
    }

    @Bean
    JacksonJsonMessageConverter jacksonJsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

}
