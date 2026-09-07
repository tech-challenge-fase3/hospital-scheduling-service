package com.hospital.schedulingservice.infra.persistence.mappers;

import org.mapstruct.Mapper;

import com.hospital.schedulingservice.domain.appointment.Appointment;
import com.hospital.schedulingservice.infra.controller.dto.AppointmentResponseDTO;

@Mapper(componentModel = "spring")
public interface AppointmentWebMapper {

    AppointmentResponseDTO toResponse(Appointment appointment);
}