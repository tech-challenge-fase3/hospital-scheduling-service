package com.hospital.scheduling_service.infra.controller.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AppointmentRequestDTO(
        @NotBlank(message = "O ID do paciente é obrigatório")
        String patientId,

        @NotBlank(message = "O ID do médico é obrigatório")
        String doctorId,

        @NotNull(message = "A data da consulta é obrigatória")
        LocalDateTime appointmentDate,

        String notes
) {
}
