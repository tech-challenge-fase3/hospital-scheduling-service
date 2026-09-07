package com.hospital.schedulingservice.infra.controller.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record UpdateAppointmentRequestDTO(
        @NotNull(message = "A data da consulta é obrigatória")
        @Future(message = "A data da consulta deve estar no futuro")
        LocalDateTime appointmentDate,
        String notes) {

}
