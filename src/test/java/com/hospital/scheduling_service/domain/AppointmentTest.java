package com.hospital.scheduling_service.domain;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.hospital.scheduling_service.domain.appointment.Appointment;
import com.hospital.scheduling_service.domain.appointment.InvalidAppointmentException;

class AppointmentTest {

    @Test
    void shouldRejectPastAppointments() {
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);

        assertThrows(InvalidAppointmentException.class, () ->
                Appointment.create("patient-1", "doctor-1", pastDate, "Consulta antiga"));
    }
}
