package com.hospital.schedulingservice.application.usecase;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hospital.schedulingservice.application.port.out.AppointmentEventPublisher;
import com.hospital.schedulingservice.domain.appointment.Appointment;
import com.hospital.schedulingservice.domain.appointment.AppointmentConflictException;
import com.hospital.schedulingservice.domain.repository.AppointmentRepository;

@Service
public class CreateAppointmentUseCase {

    private static final Logger log
            = LoggerFactory.getLogger(CreateAppointmentUseCase.class);

    private final AppointmentRepository appointmentRepository;
    private final AppointmentEventPublisher appointmentEventPublisher;

    public CreateAppointmentUseCase(
            AppointmentRepository appointmentRepository,
            AppointmentEventPublisher appointmentEventPublisher) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentEventPublisher = appointmentEventPublisher;
    }

    public Appointment execute(
            String patientId,
            String doctorId,
            LocalDateTime appointmentDate,
            String notes) {

        log.info(
                "Iniciando criação de agendamento: patientId={}, doctorId={}, appointmentDate={}",
                patientId,
                doctorId,
                appointmentDate
        );

        Appointment appointment = Appointment.create(
                patientId,
                doctorId,
                appointmentDate,
                notes
        );

        List<Appointment> doctorAppointments
                = appointmentRepository.findByDoctorId(doctorId);

        LocalDateTime nextAvailableAppointmentDate = findNextAvailableDate(
                appointment.getAppointmentDate(),
                doctorAppointments
        );

        if (!nextAvailableAppointmentDate.equals(appointment.getAppointmentDate())) {
            log.warn(
                    "Conflito de horário para agendamento: doctorId={}, appointmentDate={}, suggestedAppointmentDate={}",
                    doctorId,
                    appointment.getAppointmentDate(),
                    nextAvailableAppointmentDate
            );

            throw new AppointmentConflictException(
                    "O médico já possui um agendamento nesse intervalo",
                    nextAvailableAppointmentDate
            );
        }

        Appointment savedAppointment = appointmentRepository.save(appointment);

        log.info(
                "Agendamento persistido com sucesso: appointmentId={}",
                savedAppointment.getId()
        );

        appointmentEventPublisher.publishCreated(savedAppointment);

        return savedAppointment;
    }

    private LocalDateTime findNextAvailableDate(
            LocalDateTime requestedDate,
            List<Appointment> doctorAppointments) {

        LocalDateTime candidateDate = requestedDate;

        while (true) {
            LocalDateTime currentCandidateDate = candidateDate;

            List<Appointment> conflictingAppointments = doctorAppointments.stream()
                    .filter(appointment -> isWithinConflictWindow(
                    currentCandidateDate,
                    appointment.getAppointmentDate()))
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
