package com.vetvoice.appointment;

import com.vetvoice.appointment.dto.AppointmentRequestDTO;
import com.vetvoice.client.Client;
import com.vetvoice.client.ClientRepository;
import com.vetvoice.pet.Pet;
import com.vetvoice.pet.PetRepository;
import com.vetvoice.veterinarian.Veterinarian;
import com.vetvoice.veterinarian.VeterinarianRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Este teste sobe o contexto Spring inteiro e dispara VÁRIAS threads ao
// mesmo tempo tentando agendar o MESMO horário com o MESMO veterinário.
// A expectativa: só UMA thread consegue criar o agendamento; todas as
// outras devem falhar (seja pela checagem otimista, seja pelo índice
// único do banco). Isso é o teste que prova que a Fase 2 funciona de verdade
// - não só "no papel".
@SpringBootTest(properties = "spring.ai.openai.api-key=sk-test-fake-key-not-used")
class AppointmentConcurrencyTest {

    @Autowired private AppointmentService appointmentService;
    @Autowired private ClientRepository clientRepository;
    @Autowired private PetRepository petRepository;
    @Autowired private VeterinarianRepository veterinarianRepository;

    private Long petId;
    private Long vetId;

    @BeforeEach
    void setUp() {
        Client client = clientRepository.save(new Client("Cliente Teste", "cliente" + System.nanoTime() + "@teste.com", "0000"));
        Pet pet = petRepository.save(new Pet("Rex", "Cachorro", "SRD", client));
        Veterinarian vet = veterinarianRepository.save(
                new Veterinarian("Dr. Teste", "vet" + System.nanoTime() + "@teste.com", "Clínica Geral"));
        petId = pet.getId();
        vetId = vet.getId();
    }

    @Test
    void apenasUmaThreadDeveConseguirAgendarOMesmoHorario() throws InterruptedException {
        int numeroDeThreads = 10;
        LocalDateTime horario = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0);

        ExecutorService pool = Executors.newFixedThreadPool(numeroDeThreads);
        CountDownLatch largada = new CountDownLatch(1); // segura todas as threads no mesmo ponto de partida
        AtomicInteger sucessos = new AtomicInteger(0);
        AtomicInteger falhas = new AtomicInteger(0);

        for (int i = 0; i < numeroDeThreads; i++) {
            pool.submit(() -> {
                try {
                    largada.await();
                    appointmentService.create(new AppointmentRequestDTO(petId, vetId, horario, "tentativa concorrente"));
                    sucessos.incrementAndGet();
                } catch (Exception e) {
                    falhas.incrementAndGet();
                } finally {
                    // nada extra
                }
            });
        }

        largada.countDown(); // solta todas as threads ao mesmo tempo
        pool.shutdown();
        pool.awaitTermination(10, java.util.concurrent.TimeUnit.SECONDS);

        assertEquals(1, sucessos.get(), "apenas UM agendamento deveria ter sido criado com sucesso");
        assertEquals(numeroDeThreads - 1, falhas.get(), "as outras threads deveriam falhar por conflito");
    }
}
