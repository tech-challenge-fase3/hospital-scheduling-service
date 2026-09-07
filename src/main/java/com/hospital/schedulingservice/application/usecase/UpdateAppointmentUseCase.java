package com.hospital.schedulingservice.application.usecase;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hospital.schedulingservice.application.port.out.AppointmentEventPublisher;
import com.hospital.schedulingservice.domain.appointment.Appointment;
import com.hospital.schedulingservice.domain.appointment.AppointmentConflictException;
import com.hospital.schedulingservice.domain.repository.AppointmentRepository;
import com.hospital.schedulingservice.infra.controller.exception.AppointmentNotFoundException;

@Service
public class UpdateAppointmentUseCase {

    private static final Logger log
            = LoggerFactory.getLogger(UpdateAppointmentUseCase.class);

    private final AppointmentRepository appointmentRepository;
    private final AppointmentEventPublisher appointmentEventPublisher;

    public UpdateAppointmentUseCase(
            AppointmentRepository appointmentRepository,
            AppointmentEventPublisher appointmentEventPublisher) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentEventPublisher = appointmentEventPublisher;
    }

    public Appointment execute(
            UUID id,
            LocalDateTime appointmentDate,
            String notes) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(
                "Agendamento não encontrado para o ID informado"));

        Appointment updatedAppointment
                = appointment.reschedule(appointmentDate, notes);

        List<Appointment> doctorAppointments = appointmentRepository
                .findByDoctorId(appointment.getDoctorId())
                .stream()
                .filter(otherAppointment -> !otherAppointment.getId().equals(id))
                .toList();

        LocalDateTime nextAvailableAppointmentDate = findNextAvailableDate(
                updatedAppointment.getAppointmentDate(),
                doctorAppointments
        );

        if (!nextAvailableAppointmentDate.equals(updatedAppointment.getAppointmentDate())) {
            log.warn(
                    "Conflito ao reagendar: appointmentId={}, doctorId={}, appointmentDate={}, suggestedAppointmentDate={}",
                    id,
                    appointment.getDoctorId(),
                    updatedAppointment.getAppointmentDate(),
                    nextAvailableAppointmentDate
            );

            throw new AppointmentConflictException(
                    "O médico já possui um agendamento nesse intervalo",
                    nextAvailableAppointmentDate
            );
        }

        Appointment savedAppointment = appointmentRepository.save(updatedAppointment);

        appointmentEventPublisher.publishUpdated(savedAppointment);

        return savedAppointment;
    }

    private LocalDateTime findNextAvailableDate(
            LocalDateTime requestedDate,
            List<Appointment> doctorAppointments) {

        LocalDateTime candidateDate = requestedDate;

        while (true) {
            LocalDateTime currentCandidateDate = candidateDate;

            List<Appointment> conflictingAppointments = doctorAppointments.stream()
                    .filter(otherAppointment -> isWithinConflictWindow(
                    currentCandidateDate,
                    otherAppointment.getAppointmentDate()))
                    .toList();

            if (conflictingAppointments.isEmpty()) {
                return candidateDate;
            }

            LocalDateTime latestConflictDate = conflictingAppointments.stream()
                    .map(Appointment::getAppointmentDate)
                    .max(LocalDateTime::compareTo)
                    .orElseThrow();

            candidateDate = latestConflictDate.plusMinutes(30);
        }
    }

    private boolean isWithinConflictWindow(
            LocalDateTime firstDate,
            LocalDateTime secondDate) {

        return Math.abs(ChronoUnit.MINUTES.between(firstDate, secondDate)) < 30;
    }
}
