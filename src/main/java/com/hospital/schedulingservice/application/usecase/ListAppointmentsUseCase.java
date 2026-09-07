package com.hospital.schedulingservice.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hospital.schedulingservice.domain.appointment.Appointment;
import com.hospital.schedulingservice.domain.repository.AppointmentRepository;

@Service
public class ListAppointmentsUseCase {

    private final AppointmentRepository appointmentRepository;

    public ListAppointmentsUseCase(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> execute() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> executeForPatient(String patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }
}
