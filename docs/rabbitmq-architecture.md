# Arquitetura de Eventos RabbitMQ

## Contexto

O `hospital-scheduling-service` e o produtor dos eventos de agendamento. Ele salva o agendamento no PostgreSQL e publica eventos no RabbitMQ.

O consumo dos eventos sera responsabilidade de outro microsservico, inicialmente chamado `notification-service`.

## Responsabilidades

### scheduling-service

- Criar e atualizar agendamentos.
- Persistir o estado no PostgreSQL.
- Publicar eventos depois do `save` bem-sucedido.
- Nao usar `@RabbitListener` nesta etapa.
- Nao processar lembretes ou consumir as filas.

### notification-service

- Consumir os eventos de criacao e atualizacao.
- Processar lembretes e notificacoes.
- Usar ACK manual.
- Confirmar a mensagem somente depois do processamento bem-sucedido.
- Reenfileirar ou encaminhar para retry quando o agendamento ainda nao existir ou o processamento falhar.
- Processar mensagens de forma idempotente.

## Filas

As filas permanecem separadas:

- `hospital.appointments.created`
- `hospital.appointments.updated`

Exchange:

- `hospital.appointments`

Routing keys:

- `appointment.created`
- `appointment.updated`

Fluxo:

```text
POST appointment
  -> PostgreSQL
  -> hospital.appointments
  -> appointment.created
  -> hospital.appointments.created

PUT appointment
  -> PostgreSQL
  -> hospital.appointments
  -> appointment.updated
  -> hospital.appointments.updated
```

## Ordem e consistencia

Filas separadas nao garantem uma ordenacao global entre `created` e `updated`. Por isso, o consumidor deve:

1. Nao confirmar um `updated` se o registro ainda nao existir.
2. Reenfileirar ou encaminhar para uma fila de retry.
3. Usar `eventId` para evitar processamento duplicado.
4. Usar `appointmentId` para correlacionar eventos.
5. Usar `version` ou `updatedAt` para ignorar eventos antigos.

## Payload recomendado

Os eventos devem conter metadados de integracao:

```json
{
  "eventId": "uuid-do-evento",
  "eventType": "APPOINTMENT_CREATED",
  "appointmentId": "uuid-do-agendamento",
  "patientId": "uuid-do-paciente",
  "doctorId": "uuid-do-medico",
  "appointmentDate": "2026-09-10T14:30:00",
  "status": "SCHEDULED",
  "createdAt": "2026-09-07T21:33:04.681",
  "updatedAt": "2026-09-07T21:33:04.681",
  "version": 1
}
```

Para atualizacao, `eventType` sera `APPOINTMENT_UPDATED` e `version` devera ser maior que a versao anterior.

## Estado atual

Ja implementado no `scheduling-service`:

- Evento de criacao.
- Evento de atualizacao.
- Publisher RabbitMQ.
- Exchange `hospital.appointments`.
- Filas de criacao e atualizacao.
- Bindings das duas filas.
- Logs de publicacao.

Ainda pendente no microsservico consumidor:

- `@RabbitListener` com ACK manual.
- Politica de retry.
- Idempotencia por `eventId`.
- Controle de ordenacao por `appointmentId` e `version`.
- Processamento dos lembretes.

## Decisoes

- Nao substituir o evento de criacao pelo evento de atualizacao.
- Nao remover mensagens manualmente do produtor.
- Nao transformar POST repetido em PUT.
- Manter as filas separadas e tratar ordenacao no consumidor.
- Considerar Outbox Pattern em uma evolucao futura para garantir a consistencia entre banco e RabbitMQ.
