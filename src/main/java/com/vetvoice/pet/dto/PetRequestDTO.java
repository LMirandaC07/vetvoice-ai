package com.vetvoice.pet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PetRequestDTO(
        @NotBlank(message = "nome é obrigatório") String name,
        @NotBlank(message = "espécie é obrigatória") String species,
        String breed,
        @NotNull(message = "clientId é obrigatório") Long clientId
) {}
