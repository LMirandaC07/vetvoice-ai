package com.vetvoice.pet;

import com.vetvoice.pet.dto.PetRequestDTO;
import com.vetvoice.pet.dto.PetResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    private final PetService service;

    public PetController(PetService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PetResponseDTO> create(@Valid @RequestBody PetRequestDTO dto) {
        Pet saved = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(PetResponseDTO.from(saved));
    }

    @GetMapping("/{id}")
    public PetResponseDTO findById(@PathVariable Long id) {
        return PetResponseDTO.from(service.findById(id));
    }

    @GetMapping
    public List<PetResponseDTO> findAll(@RequestParam(required = false) Long clientId) {
        List<Pet> pets = clientId != null ? service.findByClient(clientId) : service.findAll();
        return pets.stream().map(PetResponseDTO::from).toList();
    }
}
