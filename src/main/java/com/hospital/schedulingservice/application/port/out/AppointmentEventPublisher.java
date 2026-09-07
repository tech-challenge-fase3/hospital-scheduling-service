package com.hospital.schedulingservice.application.port.out;

import com.hospital.schedulingservice.domain.appointment.Appointment;

public interface AppointmentEventPublisher {

    void publishCreated(Appointment appointment);

    void publishUpdated(Appointment appointment);
}
