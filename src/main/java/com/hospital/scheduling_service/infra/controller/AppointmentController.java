package com.hospital.scheduling_service.infra.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.scheduling_service.application.AppointmentService;
import com.hospital.scheduling_service.infra.controller.docs.AppointmentControllerDocs;
import com.hospital.scheduling_service.infra.controller.dto.AppointmentRequestDTO;
import com.hospital.scheduling_service.infra.controller.dto.AppointmentResponseDTO;

@RestController
public class AppointmentController implements AppointmentControllerDocs {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Override
    public ResponseEntity<AppointmentResponseDTO> create(AppointmentRequestDTO request) {
        AppointmentResponseDTO response = appointmentService.createAppointment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<List<AppointmentResponseDTO>> listAll() {
        List<AppointmentResponseDTO> appointments = appointmentService.findAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    @Override
    public ResponseEntity<AppointmentResponseDTO> findById(UUID id) {
        AppointmentResponseDTO response = appointmentService.findById(id);
        return ResponseEntity.ok(response);
    }
}