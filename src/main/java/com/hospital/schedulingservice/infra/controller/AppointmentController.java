package com.hospital.schedulingservice.infra.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.schedulingservice.application.usecase.CreateAppointmentUseCase;
import com.hospital.schedulingservice.application.usecase.FindAppointmentByIdUseCase;
import com.hospital.schedulingservice.application.usecase.ListAppointmentsUseCase;
import com.hospital.schedulingservice.application.usecase.UpdateAppointmentUseCase;
import com.hospital.schedulingservice.domain.appointment.Appointment;
import com.hospital.schedulingservice.infra.controller.docs.AppointmentControllerDocs;
import com.hospital.schedulingservice.infra.controller.dto.AppointmentRequestDTO;
import com.hospital.schedulingservice.infra.controller.dto.AppointmentResponseDTO;
import com.hospital.schedulingservice.infra.controller.dto.UpdateAppointmentRequestDTO;
import com.hospital.schedulingservice.infra.persistence.mappers.AppointmentWebMapper;

import jakarta.validation.Valid;

@RestController
public class AppointmentController implements AppointmentControllerDocs {

    private final AppointmentWebMapper appointmentWebMapper;
    private final CreateAppointmentUseCase createAppointmentUseCase;
    private final FindAppointmentByIdUseCase findAppointmentByIdUseCase;
    private final ListAppointmentsUseCase listAppointmentsUseCase;
    private final UpdateAppointmentUseCase updateAppointmentUseCase;

    public AppointmentController(
            CreateAppointmentUseCase createAppointmentUseCase,
            FindAppointmentByIdUseCase findAppointmentByIdUseCase,
            AppointmentWebMapper appointmentWebMapper,
            ListAppointmentsUseCase listAppointmentsUseCase,
            UpdateAppointmentUseCase updateAppointmentUseCase) {

        this.createAppointmentUseCase = createAppointmentUseCase;
        this.appointmentWebMapper = appointmentWebMapper;
        this.findAppointmentByIdUseCase = findAppointmentByIdUseCase;
        this.listAppointmentsUseCase = listAppointmentsUseCase;
        this.updateAppointmentUseCase = updateAppointmentUseCase;
    }

    @Override
    public ResponseEntity<AppointmentResponseDTO> create(@Valid AppointmentRequestDTO request) {
        Appointment appointment = createAppointmentUseCase.execute(
                request.patientId(),
                request.doctorId(),
                request.appointmentDate(),
                request.notes()
        );

        AppointmentResponseDTO response
                = appointmentWebMapper.toResponse(appointment);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<List<AppointmentResponseDTO>> listAll(Jwt jwt) {
        List<Appointment> domainAppointments = isPatient(jwt)
                ? listAppointmentsUseCase.executeForPatient(requirePatientId(jwt))
                : listAppointmentsUseCase.execute();

        List<AppointmentResponseDTO> appointments
                = domainAppointments
                        .stream()
                        .map(appointmentWebMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(appointments);
    }

    @Override
    public ResponseEntity<AppointmentResponseDTO> findById(UUID id, Jwt jwt) {
        Appointment appointment = isPatient(jwt)
                ? findAppointmentByIdUseCase.executeForPatient(id, requirePatientId(jwt))
                : findAppointmentByIdUseCase.execute(id);
        AppointmentResponseDTO response
                = appointmentWebMapper.toResponse(appointment);
        return ResponseEntity.ok(response);
    }

    private boolean isPatient(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
        if (realmAccess == null) {
            return false;
        }

        Object roles = realmAccess.get("roles");
        return roles instanceof List<?> roleList
                && roleList.stream().map(Object::toString).anyMatch("PATIENT"::equals);
    }

    private String requirePatientId(Jwt jwt) {
        String patientId = jwt.getClaimAsString("patientId");
        if (patientId == null || patientId.isBlank()) {
            throw new AccessDeniedException(
                    "O usuário paciente não possui um patientId associado");
        }
        return patientId;
    }

    @Override
    public ResponseEntity<AppointmentResponseDTO> update(UUID id, @Valid UpdateAppointmentRequestDTO request) {
        Appointment appointment = updateAppointmentUseCase.execute(
                id,
                request.appointmentDate(),
                request.notes()
        );

        AppointmentResponseDTO response
                = appointmentWebMapper.toResponse(appointment);

        return ResponseEntity.ok(response);
    }

}
