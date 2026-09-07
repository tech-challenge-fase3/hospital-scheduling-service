package com.hospital.schedulingservice.domain.appointment;

import java.time.LocalDateTime;

public class AppointmentConflictException extends RuntimeException {

    private final LocalDateTime suggestedAppointmentDate;

    public AppointmentConflictException(String message) {
        this(message, null);
    }

    public AppointmentConflictException(
            String message,
            LocalDateTime suggestedAppointmentDate) {
        super(message);
        this.suggestedAppointmentDate = suggestedAppointmentDate;
    }

    public LocalDateTime getSuggestedAppointmentDate() {
        return suggestedAppointmentDate;
    }
}
