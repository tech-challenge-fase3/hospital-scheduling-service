package com.hospital.schedulingservice.infra.messaging;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.hospital.schedulingservice.application.event.AppointmentCreatedEvent;
import com.hospital.schedulingservice.application.event.AppointmentUpdatedEvent;
import com.hospital.schedulingservice.application.port.out.AppointmentEventPublisher;
import com.hospital.schedulingservice.domain.appointment.Appointment;
import com.hospital.schedulingservice.infra.messaging.config.RabbitMQConfig;
import com.hospital.schedulingservice.infra.messaging.mappers.AppointmentEventMapper;

@Component
public class RabbitAppointmentEventPublisherAdapter
        implements AppointmentEventPublisher {

    private static final Logger log
            = LoggerFactory.getLogger(RabbitAppointmentEventPublisherAdapter.class);

    private final RabbitTemplate rabbitTemplate;
    private final AppointmentEventMapper appointmentEventMapper;

    public RabbitAppointmentEventPublisherAdapter(
            RabbitTemplate rabbitTemplate,
            AppointmentEventMapper appointmentEventMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.appointmentEventMapper = appointmentEventMapper;
    }

    @Override
    public void publishCreated(Appointment appointment) {
        log.info(
                "Publicando evento de agendamento criado: appointmentId={}, exchange={}, routingKey={}",
                appointment.getId(),
                RabbitMQConfig.APPOINTMENT_EXCHANGE,
                RabbitMQConfig.APPOINTMENT_CREATED_ROUTING_KEY
        );

        try {
            AppointmentCreatedEvent event
                    = appointmentEventMapper.toCreatedEvent(
                            appointment,
                            UUID.randomUUID()
                    );

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.APPOINTMENT_EXCHANGE,
                    RabbitMQConfig.APPOINTMENT_CREATED_ROUTING_KEY,
                    event
            );

            log.info(
                    "Evento de agendamento publicado com sucesso: appointmentId={}",
                    appointment.getId()
            );
        } catch (RuntimeException exception) {
            log.error(
                    "Falha ao publicar evento de agendamento: appointmentId={}, exchange={}, routingKey={}",
                    appointment.getId(),
                    RabbitMQConfig.APPOINTMENT_EXCHANGE,
                    RabbitMQConfig.APPOINTMENT_CREATED_ROUTING_KEY,
                    exception
            );

            throw exception;
        }
    }

    @Override
    public void publishUpdated(Appointment appointment) {
        log.info(
                "Publicando evento de agendamento atualizado: appointmentId={}, exchange={}, routingKey={}",
                appointment.getId(),
                RabbitMQConfig.APPOINTMENT_EXCHANGE,
                RabbitMQConfig.APPOINTMENT_UPDATED_ROUTING_KEY
        );

        try {
            AppointmentUpdatedEvent event
                    = appointmentEventMapper.toUpdatedEvent(
                            appointment,
                            UUID.randomUUID()
                    );

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.APPOINTMENT_EXCHANGE,
                    RabbitMQConfig.APPOINTMENT_UPDATED_ROUTING_KEY,
                    event
            );

            log.info(
                    "Evento de agendamento atualizado publicado com sucesso: appointmentId={}",
                    appointment.getId()
            );
        } catch (RuntimeException exception) {
            log.error(
                    "Falha ao publicar evento de agendamento atualizado: appointmentId={}, exchange={}, routingKey={}",
                    appointment.getId(),
                    RabbitMQConfig.APPOINTMENT_EXCHANGE,
                    RabbitMQConfig.APPOINTMENT_UPDATED_ROUTING_KEY,
                    exception
            );

            throw exception;
        }
    }
}
