package com.vetvoice.veterinarian.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VeterinarianRequestDTO(
        @NotBlank(message = "nome é obrigatório") String name,
        @NotBlank @Email(message = "email inválido") String email,
        @NotBlank(message = "especialidade é obrigatória") String specialty
) {}
