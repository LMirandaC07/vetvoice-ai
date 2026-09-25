package com.vetvoice.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Query derivada: busca agendamentos ativos (não cancelados) de um
    // veterinário num horário específico. Usamos isso na validação do
    // Service ANTES de tentar salvar - é uma checagem "otimista" que dá uma
    // resposta rápida e amigável (400) na maioria dos casos. Quem garante a
    // regra de verdade sob concorrência é o índice único do banco.
    @Query("""
        SELECT a FROM Appointment a
        WHERE a.veterinarian.id = :vetId
          AND a.scheduledAt = :scheduledAt
          AND a.status <> com.vetvoice.appointment.AppointmentStatus.CANCELLED
        """)
    List<Appointment> findActiveByVetAndSlot(@Param("vetId") Long vetId,
                                              @Param("scheduledAt") LocalDateTime scheduledAt);

    List<Appointment> findByVeterinarianId(Long veterinarianId);

    List<Appointment> findByPetId(Long petId);
}
