package com.vetvoice.veterinarian;

import jakarta.persistence.*;

@Entity
@Table(name = "veterinarian")
public class Veterinarian {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String specialty;

    protected Veterinarian() {}

    public Veterinarian(String name, String email, String specialty) {
        this.name = name;
        this.email = email;
        this.specialty = specialty;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getSpecialty() { return specialty; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
}
