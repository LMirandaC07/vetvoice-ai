package com.vetvoice.client.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// record: classe imutável, gera automaticamente construtor, getters (sem
// "get", ex: name()), equals/hashCode/toString. Ideal pra DTO, que é só
// um "pacote de dados" sem comportamento.
public record ClientRequestDTO(
        @NotBlank(message = "nome é obrigatório") String name,
        @NotBlank @Email(message = "email inválido") String email,
        String phone
) {}
