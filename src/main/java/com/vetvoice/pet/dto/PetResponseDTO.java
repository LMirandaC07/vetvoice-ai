package com.vetvoice.pet.dto;

import com.vetvoice.pet.Pet;

public record PetResponseDTO(Long id, String name, String species, String breed, Long clientId) {
    public static PetResponseDTO from(Pet pet) {
        return new PetResponseDTO(pet.getId(), pet.getName(), pet.getSpecies(), pet.getBreed(), pet.getClient().getId());
    }
}
