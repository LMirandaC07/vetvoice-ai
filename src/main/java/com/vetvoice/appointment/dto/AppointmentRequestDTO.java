package com.vetvoice.appointment.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record AppointmentRequestDTO(
        @NotNull(message = "petId é obrigatório") Long petId,
        @NotNull(message = "veterinarianId é obrigatório") Long veterinarianId,
        @NotNull(message = "scheduledAt é obrigatório")
        @Future(message = "não é possível agendar em uma data/hora no passado")
        LocalDateTime scheduledAt,
        String notes
) {}
