package com.hospital.schedulingservice.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.hospital.schedulingservice.domain.appointment.Appointment;

public interface AppointmentRepository {

    Appointment save(Appointment appointment);

    List<Appointment> findByDoctorId(String doctorId);

    Optional<Appointment> findById(UUID id);

    List<Appointment> findAll();

}
