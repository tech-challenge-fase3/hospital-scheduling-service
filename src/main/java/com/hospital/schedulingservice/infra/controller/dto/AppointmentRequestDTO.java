package com.hospital.schedulingservice.infra.controller.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AppointmentRequestDTO(
        @NotBlank(message = "O ID do pacaaiente é obrigatório")
        String patientId,

        @NotBlank(message = "O ID do médico é obrigatório")
        String doctorId,

        @NotNull(message = "A data da consulta é obrigatória")
        @Future (message = "A data da consulta deve ser no futuro")
        LocalDateTime appointmentDate,

        String notes
) {
}
