package com.vetvoice.client;

import com.vetvoice.client.dto.ClientRequestDTO;
import com.vetvoice.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {

    private final ClientRepository repository;

    // InjeÃ§Ã£o de dependÃªncia via construtor (nÃ£o @Autowired em campo).
    // Vantagens: fica explÃ­cito o que a classe precisa pra funcionar, e
    // facilita muito escrever teste unitÃ¡rio (vocÃª sÃ³ passa um mock aqui).
    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Client create(ClientRequestDTO dto) {
        Client client = new Client(dto.name(), dto.email(), dto.phone());
        return repository.save(client);
    }

    public Client findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente nÃ£o encontrado: id=" + id));
    }

    public java.util.List<Client> findAll() {
        return repository.findAll();
    }

    @Transactional
    public Client update(Long id, ClientRequestDTO dto) {
        Client client = findById(id);
        client.setName(dto.name());
        client.setEmail(dto.email());
        client.setPhone(dto.phone());
        return client; // dentro de uma transaÃ§Ã£o, o Hibernate detecta a mudanÃ§a e faz UPDATE sozinho (dirty checking)
    }

    @Transactional
    public void delete(Long id) {
        Client client = findById(id);
        repository.delete(client);
    }
}

