package com.hospital.schedulingservice.infra.messaging.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.hospital.schedulingservice.application.event.AppointmentCreatedEvent;
import com.hospital.schedulingservice.application.event.AppointmentUpdatedEvent;
import com.hospital.schedulingservice.domain.appointment.Appointment;

@Mapper(componentModel = "spring")
public interface AppointmentEventMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "patientId", target = "patientId")
    @Mapping(source = "doctorId", target = "doctorId")
    @Mapping(source = "appointmentDate", target = "appointmentDate")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "notes", target = "notes")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    AppointmentCreatedEvent toCreatedEvent(Appointment appointment);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "patientId", target = "patientId")
    @Mapping(source = "doctorId", target = "doctorId")
    @Mapping(source = "appointmentDate", target = "appointmentDate")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "notes", target = "notes")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    AppointmentUpdatedEvent toUpdatedEvent(Appointment appointment);
}
