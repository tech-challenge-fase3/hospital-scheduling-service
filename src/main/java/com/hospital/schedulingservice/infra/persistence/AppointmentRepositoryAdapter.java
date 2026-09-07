package com.hospital.schedulingservice.infra.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.hospital.schedulingservice.domain.appointment.Appointment;
import com.hospital.schedulingservice.domain.repository.AppointmentRepository;
import com.hospital.schedulingservice.infra.persistence.mappers.AppointmentPersistenceMapper;

@Repository
public class AppointmentRepositoryAdapter implements AppointmentRepository {

    private static final Logger log
            = LoggerFactory.getLogger(AppointmentRepositoryAdapter.class);

    private final AppointmentJpaRepository appointmentJpaRepository;
    private final AppointmentPersistenceMapper appointmentPersistenceMapper;

    public AppointmentRepositoryAdapter(AppointmentJpaRepository appointmentJpaRepository, AppointmentPersistenceMapper appointmentPersistenceMapper) {
        this.appointmentJpaRepository = appointmentJpaRepository;
        this.appointmentPersistenceMapper = appointmentPersistenceMapper;
    }

    @Override
    public Appointment save(Appointment appointment) {
        log.info(
                "Persistindo agendamento: appointmentId={}, patientId={}, doctorId={}",
                appointment.getId(),
                appointment.getPatientId(),
                appointment.getDoctorId()
        );

        try {
            AppointmentEntity entity
                    = appointmentPersistenceMapper.toEntity(appointment);

            AppointmentEntity savedEntity
                    = appointmentJpaRepository.save(entity);

            Appointment savedAppointment
                    = appointmentPersistenceMapper.toDomain(savedEntity);

            log.info(
                    "Agendamento persistido: appointmentId={}",
                    savedAppointment.getId()
            );

            return savedAppointment;
        } catch (RuntimeException exception) {
            log.error(
                    "Falha ao persistir agendamento: appointmentId={}",
                    appointment.getId(),
                    exception
            );

            throw exception;
        }
    }

    @Override
    public List<Appointment> findByDoctorId(String doctorId) {
        return appointmentJpaRepository.findByDoctorIdOrderByAppointmentDateAsc(doctorId)
                .stream()
                .map(appointmentPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Appointment> findById(UUID id) {
        return appointmentJpaRepository.findById(id)
                .map(appointmentPersistenceMapper::toDomain);
    }

    @Override
    public List<Appointment> findAll() {
        return appointmentJpaRepository.findAll().stream()
                .map(appointmentPersistenceMapper::toDomain)
                .toList();
    }

}
