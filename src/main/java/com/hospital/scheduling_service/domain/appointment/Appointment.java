package com.hospital.scheduling_service.domain.appointment;

import java.time.LocalDateTime;
import java.util.UUID;

public class Appointment {

    private final UUID id;
    private final String patientId;
    private final String doctorId;
    private final LocalDateTime appointmentDate;
    private final String notes;
    private final String status;
    private final LocalDateTime createdAt;

    private Appointment(UUID id, String patientId, String doctorId, LocalDateTime appointmentDate,
                        String notes, String status, LocalDateTime createdAt) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.notes = notes;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static Appointment create(String patientId, String doctorId, LocalDateTime appointmentDate, String notes) {
        validate(patientId, doctorId, appointmentDate);

        return new Appointment(
                UUID.randomUUID(),
                patientId,
                doctorId,
                appointmentDate,
                notes,
                "SCHEDULED",
                LocalDateTime.now()
        );
    }

    private static void validate(String patientId, String doctorId, LocalDateTime appointmentDate) {
        if (patientId == null || patientId.isBlank()) {
            throw new InvalidAppointmentException("O ID do paciente é obrigatório");
        }

        if (doctorId == null || doctorId.isBlank()) {
            throw new InvalidAppointmentException("O ID do médico é obrigatório");
        }

        if (appointmentDate == null) {
            throw new InvalidAppointmentException("A data da consulta é obrigatória");
        }

        if (appointmentDate.isBefore(LocalDateTime.now())) {
            throw new InvalidAppointmentException("A data da consulta deve ser no futuro");
        }
    }

    public UUID getId() {
        return id;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public String getNotes() {
        return notes;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
