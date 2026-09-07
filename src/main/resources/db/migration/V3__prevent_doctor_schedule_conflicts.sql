CREATE UNIQUE INDEX uk_appointments_doctor_date
    ON appointments (doctor_id, appointment_date);