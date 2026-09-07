package com.hospital.schedulingservice.infra.controller.docs;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hospital.schedulingservice.infra.controller.dto.AppointmentRequestDTO;
import com.hospital.schedulingservice.infra.controller.dto.AppointmentResponseDTO;
import com.hospital.schedulingservice.infra.controller.dto.UpdateAppointmentRequestDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Appointments", description = "Endpoints para gerenciamento de agendamentos médicos")
@RequestMapping("/api/v1/appointments")
public interface AppointmentControllerDocs {

    @Operation(summary = "Criar um novo agendamento", description = "Realiza o cadastro de um novo agendamento no sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Agendamento criado com sucesso",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = AppointmentResponseDTO.class),
                        examples = @ExampleObject(
                                name = "Agendamento criado",
                                value = "{\n  \"id\": \"0f2f4a76-5a65-4f8a-9b6f-d4e8b07a5ab1\",\n  \"patientId\": \"5d8b0d5c-8d8c-4a1f-9b7b-2c7d9e7d4a1f\",\n  \"doctorId\": \"3a6f8f90-42a4-4b1c-b367-4d0eae6c62c7\",\n  \"appointmentDate\": \"2026-09-15T10:30:00\",\n  \"status\": \"SCHEDULED\",\n  \"notes\": \"Consulta de retorno\",\n  \"createdAt\": \"2026-09-04T09:15:00\"\n}"
                        )
                )
        ),
        @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos ou violação de validação",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Erro de validação",
                                value = "{\n  \"timestamp\": \"2026-09-04T09:15:00Z\",\n  \"status\": 400,\n  \"error\": \"Bad Request\",\n  \"message\": \"O ID do paciente é obrigatório\",\n  \"path\": \"/api/v1/appointments\"\n}"
                        )
                )
        ),
        @ApiResponse(responseCode = "409", description = "Conflito de horário ou erro de integridade",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Erro de regra de negócio",
                                value = "{\n  \"timestamp\": \"2026-09-04T09:15:00Z\",\n  \"status\": 409,\n  \"error\": \"Conflict\",\n  \"message\": \"O médico já possui um agendamento nesse intervalo\",\n  \"suggestedAppointmentDate\": \"2026-09-10T15:00:00\",\n  \"path\": \"/api/v1/appointments\"\n}"
                        )
                )
        ),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Erro interno",
                                value = "{\n  \"timestamp\": \"2026-09-04T09:15:00Z\",\n  \"status\": 500,\n  \"error\": \"Internal Server Error\",\n  \"message\": \"Erro inesperado ao processar a solicitação\",\n  \"path\": \"/api/v1/appointments\"\n}"
                        )
                )
        )
    })
    @PostMapping
    ResponseEntity<AppointmentResponseDTO> create(
            @RequestBody @Valid AppointmentRequestDTO request
    );

    @Operation(
            summary = "Atualizar um agendamento",
            description = "Altera a data e as observações de um agendamento existente."
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Agendamento atualizado com sucesso",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(
                                implementation = AppointmentResponseDTO.class
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Dados inválidos"
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Agendamento não encontrado"
        ),
        @ApiResponse(
                responseCode = "409",
                description = "Conflito de horário ou erro de integridade",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Conflito ao reagendar",
                                value = "{\n  \"timestamp\": \"2026-09-07T20:27:47Z\",\n  \"status\": 409,\n  \"error\": \"Conflict\",\n  \"message\": \"O médico já possui um agendamento nesse intervalo\",\n  \"suggestedAppointmentDate\": \"2026-09-10T16:00:00\",\n  \"path\": \"/api/v1/appointments/{id}\"\n}"
                        )
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Erro interno no servidor"
        )
    })
    @PutMapping("/{id}")
    ResponseEntity<AppointmentResponseDTO> update(
            @PathVariable("id") UUID id,
            @RequestBody @Valid UpdateAppointmentRequestDTO request
    );

    @Operation(summary = "Listar todos os agendamentos", description = "Retorna uma lista com todos os agendamentos cadastrados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
                content = @Content(
                        mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = AppointmentResponseDTO.class)),
                        examples = @ExampleObject(
                                name = "Lista de agendamentos",
                                value = "[{\n  \"id\": \"0f2f4a76-5a65-4f8a-9b6f-d4e8b07a5ab1\",\n  \"patientId\": \"5d8b0d5c-8d8c-4a1f-9b7b-2c7d9e7d4a1f\",\n  \"doctorId\": \"3a6f8f90-42a4-4b1c-b367-4d0eae6c62c7\",\n  \"appointmentDate\": \"2026-09-15T10:30:00\",\n  \"status\": \"SCHEDULED\",\n  \"notes\": \"Consulta de retorno\",\n  \"createdAt\": \"2026-09-04T09:15:00\"\n}, {\n  \"id\": \"2a9b1af2-c216-4d1d-9325-7d8f1d7d5ad4\",\n  \"patientId\": \"c7d8cd10-27f4-42a2-bced-1b2d4fd7c0bc\",\n  \"doctorId\": \"9d7a8611-0d4b-46b5-a1be-8ecf5a9b4aa2\",\n  \"appointmentDate\": \"2026-09-16T14:00:00\",\n  \"status\": \"SCHEDULED\",\n  \"notes\": \"Avaliação inicial\",\n  \"createdAt\": \"2026-09-04T09:21:00\"\n}]"
                        )
                )
        ),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Erro ao listar",
                                value = "{\n  \"timestamp\": \"2026-09-04T09:15:00Z\",\n  \"status\": 500,\n  \"error\": \"Internal Server Error\",\n  \"message\": \"Erro inesperado ao consultar agendamentos\",\n  \"path\": \"/api/v1/appointments\"\n}"
                        )
                )
        )
    })
    @GetMapping
    ResponseEntity<List<AppointmentResponseDTO>> listAll();

    @Operation(summary = "Buscar agendamento por ID", description = "Retorna os detalhes de um agendamento específico através do seu ID único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Agendamento encontrado com sucesso",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = AppointmentResponseDTO.class),
                        examples = @ExampleObject(
                                name = "Agendamento encontrado",
                                value = "{\n  \"id\": \"0f2f4a76-5a65-4f8a-9b6f-d4e8b07a5ab1\",\n  \"patientId\": \"5d8b0d5c-8d8c-4a1f-9b7b-2c7d9e7d4a1f\",\n  \"doctorId\": \"3a6f8f90-42a4-4b1c-b367-4d0eae6c62c7\",\n  \"appointmentDate\": \"2026-09-15T10:30:00\",\n  \"status\": \"SCHEDULED\",\n  \"notes\": \"Consulta de retorno\",\n  \"createdAt\": \"2026-09-04T09:15:00\"\n}"
                        )
                )
        ),
        @ApiResponse(responseCode = "400", description = "ID informado em formato inválido",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "UUID inválido",
                                value = "{\n  \"timestamp\": \"2026-09-04T09:15:00Z\",\n  \"status\": 400,\n  \"error\": \"Bad Request\",\n  \"message\": \"ID informado em formato inválido\",\n  \"path\": \"/api/v1/appointments/123\"\n}"
                        )
                )
        ),
        @ApiResponse(responseCode = "404", description = "Agendamento não encontrado",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Não encontrado",
                                value = "{\n  \"timestamp\": \"2026-09-04T09:15:00Z\",\n  \"status\": 404,\n  \"error\": \"Not Found\",\n  \"message\": \"Agendamento não encontrado para o ID informado\",\n  \"path\": \"/api/v1/appointments/0f2f4a76-5a65-4f8a-9b6f-d4e8b07a5ab1\"\n}"
                        )
                )
        ),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Erro interno",
                                value = "{\n  \"timestamp\": \"2026-09-04T09:15:00Z\",\n  \"status\": 500,\n  \"error\": \"Internal Server Error\",\n  \"message\": \"Erro inesperado ao buscar agendamento\",\n  \"path\": \"/api/v1/appointments/0f2f4a76-5a65-4f8a-9b6f-d4e8b07a5ab1\"\n}"
                        )
                )
        )
    })
    @GetMapping("/{id}")
    ResponseEntity<AppointmentResponseDTO> findById(@PathVariable("id") UUID id);
}
