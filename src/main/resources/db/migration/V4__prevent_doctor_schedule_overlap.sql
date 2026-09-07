CREATE EXTENSION IF NOT EXISTS btree_gist;

ALTER TABLE appointments
    ADD CONSTRAINT ex_appointments_doctor_schedule_overlap
    EXCLUDE USING gist (
        doctor_id WITH =,
        tsrange(
            appointment_date,
            appointment_date + INTERVAL '30 minutes',
            '[)'
        ) WITH &&
    );
