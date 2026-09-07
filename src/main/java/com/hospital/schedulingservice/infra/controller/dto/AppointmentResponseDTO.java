package com.hospital.schedulingservice.infra.controller.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentResponseDTO(
        UUID id,
        String patientId,
        String doctorId,
        LocalDateTime appointmentDate,
        String status,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
