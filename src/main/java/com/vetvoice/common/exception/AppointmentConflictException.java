package com.vetvoice.common.exception;

// Lançada quando já existe um agendamento ativo pro mesmo veterinário no
// mesmo horário. Mapeada para HTTP 409 CONFLICT no GlobalExceptionHandler.
public class AppointmentConflictException extends RuntimeException {
    public AppointmentConflictException(String message) {
        super(message);
    }
}
