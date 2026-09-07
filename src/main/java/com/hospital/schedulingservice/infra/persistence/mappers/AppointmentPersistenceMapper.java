package com.hospital.schedulingservice.infra.persistence.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.hospital.schedulingservice.domain.appointment.Appointment;
import com.hospital.schedulingservice.infra.persistence.AppointmentEntity;

@Mapper(componentModel = "spring")
public interface AppointmentPersistenceMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "patientId", target = "patientId")
    @Mapping(source = "doctorId", target = "doctorId")
    @Mapping(source = "appointmentDate", target = "appointmentDate")
    @Mapping(source = "notes", target = "notes")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    AppointmentEntity toEntity(Appointment appointment);

    default Appointment toDomain(AppointmentEntity entity) {
        return Appointment.reconstitute(
                entity.getId(),
                entity.getPatientId(),
                entity.getDoctorId(),
                entity.getAppointmentDate(),
                entity.getNotes(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}