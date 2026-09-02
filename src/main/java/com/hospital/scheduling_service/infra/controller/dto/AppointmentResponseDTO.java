package com.hospital.scheduling_service.infra.controller.dto;

import com.hospital.scheduling_service.infra.persistence.AppointmentEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentResponseDTO(
        UUID id,
        String patientId,
        String doctorId,
        LocalDateTime appointmentDate,
        String status,
        String notes,
        LocalDateTime createdAt
) {
    // Método utilitário para converter a Entity JPA para o DTO de resposta
    public static AppointmentResponseDTO fromEntity(AppointmentEntity entity) {
        return new AppointmentResponseDTO(
                entity.getId(),
                entity.getPatientId(),
                entity.getDoctorId(),
                entity.getAppointmentDate(),
                entity.getStatus(),
                entity.getNotes(),
                entity.getCreatedAt()
        );
    }
}
