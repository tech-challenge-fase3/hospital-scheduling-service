CREATE TABLE appointments
(
    id               UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    patient_id       VARCHAR(255) NOT NULL,
    doctor_id        VARCHAR(255) NOT NULL,
    appointment_date TIMESTAMP    NOT NULL,
    status           VARCHAR(50)  NOT NULL DEFAULT 'SCHEDULED',
    notes            TEXT,
    created_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP
);

-- Índices para otimizar as buscas por paciente e médico (essencial para o ecossistema de consultas/histórico)
CREATE INDEX idx_appointments_patient ON appointments (patient_id);
CREATE INDEX idx_appointments_doctor ON appointments (doctor_id);
CREATE INDEX idx_appointments_date ON appointments (appointment_date);