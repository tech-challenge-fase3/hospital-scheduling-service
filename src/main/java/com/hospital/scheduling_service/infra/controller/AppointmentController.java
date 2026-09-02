package com.hospital.scheduling_service.infra.controller;

import com.hospital.scheduling_service.application.AppointmentService;
import com.hospital.scheduling_service.infra.controller.dto.AppointmentRequestDTO;
import com.hospital.scheduling_service.infra.controller.dto.AppointmentResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> create(@RequestBody @Valid AppointmentRequestDTO request) {
        AppointmentResponseDTO response = appointmentService.createAppointment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<AppointmentResponseDTO>> listAll() {
        List<AppointmentResponseDTO> appointments = appointmentService.findAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> findById(@PathVariable UUID id) {
        AppointmentResponseDTO response = appointmentService.findById(id);
        return ResponseEntity.ok(response);
    }



}
