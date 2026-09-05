package com.hospital.scheduling_service.application;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hospital.scheduling_service.infra.controller.dto.AppointmentRequestDTO;
import com.hospital.scheduling_service.infra.controller.dto.AppointmentResponseDTO;
import com.hospital.scheduling_service.infra.controller.exception.AppointmentNotFoundException;
import com.hospital.scheduling_service.infra.persistence.AppointmentEntity;
import com.hospital.scheduling_service.infra.persistence.AppointmentRepository;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Transactional
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO request) {
        AppointmentEntity entity = new AppointmentEntity();
        entity.setPatientId(request.patientId());
        entity.setDoctorId(request.doctorId());
        entity.setAppointmentDate(request.appointmentDate());
        entity.setNotes(request.notes());
        // O status já assume 'SCHEDULED' por padrão na entidade

        AppointmentEntity savedEntity = appointmentRepository.save(entity);
        return AppointmentResponseDTO.fromEntity(savedEntity);
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> findAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(AppointmentResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AppointmentResponseDTO findById(UUID id) {
        AppointmentEntity entity = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Agendamento não encontrado para o ID informado"));
        return AppointmentResponseDTO.fromEntity(entity);
    }


}
