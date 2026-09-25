package com.vetvoice.veterinarian.dto;

import com.vetvoice.veterinarian.Veterinarian;

public record VeterinarianResponseDTO(Long id, String name, String email, String specialty) {
    public static VeterinarianResponseDTO from(Veterinarian vet) {
        return new VeterinarianResponseDTO(vet.getId(), vet.getName(), vet.getEmail(), vet.getSpecialty());
    }
}
