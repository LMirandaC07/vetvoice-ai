package com.vetvoice.pet;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {

    // Spring Data cria a query automaticamente a partir do nome do método:
    // "findByClientId" -> SELECT * FROM pet WHERE client_id = ?
    List<Pet> findByClientId(Long clientId);
}
