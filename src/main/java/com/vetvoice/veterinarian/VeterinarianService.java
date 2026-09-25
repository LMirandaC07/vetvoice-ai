package com.vetvoice.veterinarian;

import com.vetvoice.common.exception.ResourceNotFoundException;
import com.vetvoice.veterinarian.dto.VeterinarianRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VeterinarianService {

    private final VeterinarianRepository repository;

    public VeterinarianService(VeterinarianRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Veterinarian create(VeterinarianRequestDTO dto) {
        return repository.save(new Veterinarian(dto.name(), dto.email(), dto.specialty()));
    }

    public Veterinarian findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinário não encontrado: id=" + id));
    }

    public List<Veterinarian> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(findById(id));
    }
}
