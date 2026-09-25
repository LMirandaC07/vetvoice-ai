package com.vetvoice.client.dto;

import com.vetvoice.client.Client;

public record ClientResponseDTO(Long id, String name, String email, String phone) {

    // factory method: converte entidade -> DTO num lugar só. Evita expor
    // a entidade JPA (com proxies do Hibernate, lazy loading etc) direto na API.
    public static ClientResponseDTO from(Client client) {
        return new ClientResponseDTO(client.getId(), client.getName(), client.getEmail(), client.getPhone());
    }
}
