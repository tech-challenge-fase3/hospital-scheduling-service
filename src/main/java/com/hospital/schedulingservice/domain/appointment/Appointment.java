package com.hospital.schedulingservice.domain.appointment;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class Appointment {

    private final UUID id;
    private final String patientId;
    private final String doctorId;
    private final LocalDateTime appointmentDate;
    private final String notes;
    private final String status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private Appointment(UUID id, String patientId, String doctorId, LocalDateTime appointmentDate,
            String notes, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.notes = notes;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Appointment create(
            String patientId,
            String doctorId,
            LocalDateTime appointmentDate,
            String notes) {

        LocalDateTime normalizedAppointmentDate = normalizeAppointmentDate(appointmentDate);
        validateCreation(patientId, doctorId, normalizedAppointmentDate);
        LocalDateTime now = LocalDateTime.now();

        return new Appointment(
                null,
                patientId,
                doctorId,
                normalizedAppointmentDate,
                notes,
                "SCHEDULED",
                now,
                now
        );
    }

    public static Appointment reconstitute(
            UUID id,
            String patientId,
            String doctorId,
            LocalDateTime appointmentDate,
            String notes,
            String status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        validateReconstitution(
                id,
                patientId,
                doctorId,
                appointmentDate,
                status,
                createdAt,
                updatedAt
        );

        return new Appointment(
                id,
                patientId,
                doctorId,
                appointmentDate,
                notes,
                status,
                createdAt,
                updatedAt
        );
    }

    private static void validateCreation(
            String patientId,
            String doctorId,
            LocalDateTime appointmentDate) {

        validateRequiredFields(patientId, doctorId, appointmentDate);

        validateFutureDate(appointmentDate);
    }

    private static void validateReconstitution(
            UUID id,
            String patientId,
            String doctorId,
            LocalDateTime appointmentDate,
            String status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        if (id == null) {
            throw new InvalidAppointmentException(
                    "O ID do agendamento é obrigatório");
        }

        validateRequiredFields(patientId, doctorId, appointmentDate);

        if (status == null || status.isBlank()) {
            throw new InvalidAppointmentException(
                    "O status do agendamento é obrigatório");
        }

        if (createdAt == null) {
            throw new InvalidAppointmentException(
                    "A data de criação é obrigatória");
        }

        if (updatedAt == null) {
            throw new InvalidAppointmentException(
                    "A data de atualização é obrigatória");
        }
    }

    private static void validateRequiredFields(
            String patientId,
            String doctorId,
            LocalDateTime appointmentDate) {

        if (patientId == null || patientId.isBlank()) {
            throw new InvalidAppointmentException(
                    "O ID do paciente é obrigatório");
        }

        if (doctorId == null || doctorId.isBlank()) {
            throw new InvalidAppointmentException(
                    "O ID do médico é obrigatório");
        }

        if (appointmentDate == null) {
            throw new InvalidAppointmentException(
                    "A data da consulta é obrigatória");
        }
    }

    public Appointment reschedule(
            LocalDateTime newAppointmentDate,
            String newNotes) {

        validateReconstitution(
                this.id,
                this.patientId,
                this.doctorId,
                this.appointmentDate,
                this.status,
                this.createdAt,
                this.updatedAt
        );

        LocalDateTime normalizedAppointmentDate = normalizeAppointmentDate(newAppointmentDate);

        validateRequiredFields(
                this.patientId,
                this.doctorId,
                normalizedAppointmentDate
        );

        validateFutureDate(normalizedAppointmentDate);

        return new Appointment(
                this.id,
                this.patientId,
                this.doctorId,
                normalizedAppointmentDate,
                newNotes,
                this.status,
                this.createdAt,
                LocalDateTime.now()
        );
    }

    private static LocalDateTime normalizeAppointmentDate(
            LocalDateTime appointmentDate) {
        if (appointmentDate == null) {
            return null;
        }

        return appointmentDate.truncatedTo(ChronoUnit.MINUTES);
    }

    private static void validateFutureDate(LocalDateTime appointmentDate) {
        if (appointmentDate == null) {
            throw new InvalidAppointmentException(
                    "A data da consulta é obrigatória");
        }

        if (appointmentDate.isBefore(LocalDateTime.now())) {
            throw new InvalidAppointmentException(
                    "A data da consulta deve ser no futuro");
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
