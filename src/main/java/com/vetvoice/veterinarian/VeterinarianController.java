package com.vetvoice.veterinarian;

import com.vetvoice.veterinarian.dto.VeterinarianRequestDTO;
import com.vetvoice.veterinarian.dto.VeterinarianResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/veterinarians")
public class VeterinarianController {

    private final VeterinarianService service;

    public VeterinarianController(VeterinarianService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<VeterinarianResponseDTO> create(@Valid @RequestBody VeterinarianRequestDTO dto) {
        Veterinarian saved = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(VeterinarianResponseDTO.from(saved));
    }

    @GetMapping("/{id}")
    public VeterinarianResponseDTO findById(@PathVariable Long id) {
        return VeterinarianResponseDTO.from(service.findById(id));
    }

    @GetMapping
    public List<VeterinarianResponseDTO> findAll() {
        return service.findAll().stream().map(VeterinarianResponseDTO::from).toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
