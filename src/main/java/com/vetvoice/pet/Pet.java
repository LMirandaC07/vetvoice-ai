package com.vetvoice.pet;

import com.vetvoice.client.Client;
import jakarta.persistence.*;

@Entity
@Table(name = "pet")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String species;
    private String breed;

    // Pet é o lado DONO do relacionamento com Client: é ele que tem a
    // coluna client_id no banco. @ManyToOne sempre é o lado dono.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    protected Pet() {}

    public Pet(String name, String species, String breed, Client client) {
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.client = client;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getSpecies() { return species; }
    public String getBreed() { return breed; }
    public Client getClient() { return client; }

    public void setName(String name) { this.name = name; }
    public void setSpecies(String species) { this.species = species; }
    public void setBreed(String breed) { this.breed = breed; }
}
