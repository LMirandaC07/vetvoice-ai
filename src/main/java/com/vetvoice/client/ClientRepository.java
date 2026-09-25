package com.vetvoice.client;

import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository já dá de graça: save, findById, findAll, deleteById, etc.
// Não escrevemos implementação nenhuma - o Spring Data gera em runtime.
public interface ClientRepository extends JpaRepository<Client, Long> {
}
