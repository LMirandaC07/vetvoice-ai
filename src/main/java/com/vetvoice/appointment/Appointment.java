package com.vetvoice.appointment;

import com.vetvoice.pet.Pet;
import com.vetvoice.veterinarian.Veterinarian;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veterinarian_id", nullable = false)
    private Veterinarian veterinarian;

    private LocalDateTime scheduledAt;

    @Enumerated(EnumType.STRING) // grava o NOME do enum (ex: "SCHEDULED"), nao o numero da posicao
    private AppointmentStatus status;

    private String notes;

    // FASE 2 (concorrência), camada 2 de proteção: lock otimista.
    // O Hibernate usa essa coluna pra detectar se ALGUEM ALTEROU a linha entre
    // o momento em que você leu e o momento em que você tenta salvar.
    // Se sim, ele lança OptimisticLockException em vez de sobrescrever silenciosamente.
    // A camada 1 (a mais forte) é o índice único parcial no banco (V1__init_schema.sql),
    // que é quem realmente impede o double-booking em concorrência real.
    @Version
    private Long version;

    protected Appointment() {}

    public Appointment(Pet pet, Veterinarian veterinarian, LocalDateTime scheduledAt, String notes) {
        this.pet = pet;
        this.veterinarian = veterinarian;
        this.scheduledAt = scheduledAt;
        this.notes = notes;
        this.status = AppointmentStatus.SCHEDULED;
    }

    public Long getId() { return id; }
    public Pet getPet() { return pet; }
    public Veterinarian getVeterinarian() { return veterinarian; }
    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public AppointmentStatus getStatus() { return status; }
    public String getNotes() { return notes; }
    public Long getVersion() { return version; }

    public void cancel() { this.status = AppointmentStatus.CANCELLED; }
    public void confirm() { this.status = AppointmentStatus.CONFIRMED; }
    public void complete() { this.status = AppointmentStatus.COMPLETED; }
}
