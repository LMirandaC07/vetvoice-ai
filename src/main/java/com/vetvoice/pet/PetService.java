package com.vetvoice.pet;

import com.vetvoice.client.Client;
import com.vetvoice.client.ClientService;
import com.vetvoice.common.exception.ResourceNotFoundException;
import com.vetvoice.pet.dto.PetRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PetService {

    private final PetRepository repository;
    private final ClientService clientService;

    public PetService(PetRepository repository, ClientService clientService) {
        this.repository = repository;
        this.clientService = clientService;
    }

    @Transactional
    public Pet create(PetRequestDTO dto) {
        Client client = clientService.findById(dto.clientId()); // garante que o dono existe
        Pet pet = new Pet(dto.name(), dto.species(), dto.breed(), client);
        return repository.save(pet);
    }

    public Pet findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet não encontrado: id=" + id));
    }

    public List<Pet> findByClient(Long clientId) {
        return repository.findByClientId(clientId);
    }

    public List<Pet> findAll() {
        return repository.findAll();
    }
}
