package com.vetvoice.client;

import com.vetvoice.pet.Pet;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;

    // mappedBy = o lado "dono" do relacionamento é Pet (que tem a FK client_id).
    // Client é o lado inverso: só espelha a relação, não gera coluna nenhuma.
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pet> pets = new ArrayList<>();

    protected Client() {
        // construtor vazio exigido pelo JPA/Hibernate (ele usa reflection pra
        // instanciar a entidade antes de popular os campos)
    }

    public Client(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public List<Pet> getPets() { return pets; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
}
