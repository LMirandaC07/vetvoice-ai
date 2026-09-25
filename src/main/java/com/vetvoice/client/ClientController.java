package com.vetvoice.client;

import com.vetvoice.client.dto.ClientRequestDTO;
import com.vetvoice.client.dto.ClientResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ClientResponseDTO> create(@Valid @RequestBody ClientRequestDTO dto) {
        Client saved = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ClientResponseDTO.from(saved));
    }

    @GetMapping("/{id}")
    public ClientResponseDTO findById(@PathVariable Long id) {
        return ClientResponseDTO.from(service.findById(id));
    }

    @GetMapping
    public List<ClientResponseDTO> findAll() {
        return service.findAll().stream().map(ClientResponseDTO::from).toList();
    }

    @PutMapping("/{id}")
    public ClientResponseDTO update(@PathVariable Long id, @Valid @RequestBody ClientRequestDTO dto) {
        return ClientResponseDTO.from(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
