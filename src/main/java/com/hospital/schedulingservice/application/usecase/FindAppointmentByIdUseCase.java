package com.hospital.schedulingservice.application.usecase;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.hospital.schedulingservice.domain.appointment.Appointment;
import com.hospital.schedulingservice.domain.repository.AppointmentRepository;
import com.hospital.schedulingservice.infra.controller.exception.AppointmentNotFoundException;

@Service
public class FindAppointmentByIdUseCase {

    private final AppointmentRepository appointmentRepository;

    public FindAppointmentByIdUseCase(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment execute(UUID id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(
                "Agendamento não encontrado para o ID informado"));
    }

    public Appointment executeForPatient(UUID id, String patientId) {
        return appointmentRepository.findByIdAndPatientId(id, patientId)
                .orElseThrow(() -> new AppointmentNotFoundException(
                "Agendamento não encontrado ou não está disponível para consulta"));
    }
}
