package com.hospital.schedulingservice.infra.messaging.mappers;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.hospital.schedulingservice.application.event.AppointmentCreatedEvent;
import com.hospital.schedulingservice.application.event.AppointmentUpdatedEvent;
import com.hospital.schedulingservice.domain.appointment.Appointment;

@Mapper(componentModel = "spring")
public interface AppointmentEventMapper {

    @Mapping(source = "appointment.id", target = "id")
    @Mapping(source = "appointment.patientId", target = "patientId")
    @Mapping(source = "appointment.doctorId", target = "doctorId")
    @Mapping(source = "appointment.appointmentDate", target = "appointmentDate")
    @Mapping(source = "appointment.status", target = "status")
    @Mapping(source = "appointment.notes", target = "notes")
    @Mapping(source = "appointment.createdAt", target = "createdAt")
    @Mapping(source = "appointment.updatedAt", target = "updatedAt")
    @Mapping(source = "eventId", target = "eventId")
    @Mapping(target = "eventType", constant = "APPOINTMENT_CREATED")
    @Mapping(source = "appointment.id", target = "appointmentId")
    AppointmentCreatedEvent toCreatedEvent(Appointment appointment, UUID eventId);

    @Mapping(source = "appointment.id", target = "id")
    @Mapping(source = "appointment.patientId", target = "patientId")
    @Mapping(source = "appointment.doctorId", target = "doctorId")
    @Mapping(source = "appointment.appointmentDate", target = "appointmentDate")
    @Mapping(source = "appointment.status", target = "status")
    @Mapping(source = "appointment.notes", target = "notes")
    @Mapping(source = "appointment.createdAt", target = "createdAt")
    @Mapping(source = "appointment.updatedAt", target = "updatedAt")
    @Mapping(source = "eventId", target = "eventId")
    @Mapping(target = "eventType", constant = "APPOINTMENT_UPDATED")
    @Mapping(source = "appointment.id", target = "appointmentId")
    AppointmentUpdatedEvent toUpdatedEvent(Appointment appointment, UUID eventId);
}
