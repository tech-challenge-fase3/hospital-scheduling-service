package com.hospital.schedulingservice.application.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentUpdatedEvent(
        UUID eventId,
        String eventType,
        UUID appointmentId,
        UUID id,
        String patientId,
        String doctorId,
        LocalDateTime appointmentDate,
        String status,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

}
