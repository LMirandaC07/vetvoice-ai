package com.vetvoice.appointment;

import com.vetvoice.appointment.dto.AppointmentRequestDTO;
import com.vetvoice.common.exception.AppointmentConflictException;
import com.vetvoice.common.exception.ResourceNotFoundException;
import com.vetvoice.pet.Pet;
import com.vetvoice.pet.PetService;
import com.vetvoice.veterinarian.Veterinarian;
import com.vetvoice.veterinarian.VeterinarianService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository repository;
    private final PetService petService;
    private final VeterinarianService veterinarianService;

    public AppointmentService(AppointmentRepository repository,
                               PetService petService,
                               VeterinarianService veterinarianService) {
        this.repository = repository;
        this.petService = petService;
        this.veterinarianService = veterinarianService;
    }

    @Transactional
    public Appointment create(AppointmentRequestDTO dto) {
        Pet pet = petService.findById(dto.petId());
        Veterinarian vet = veterinarianService.findById(dto.veterinarianId());

        // Camada 1 de proteÃ§Ã£o contra double-booking: checagem "otimista".
        // Isso NÃƒO Ã© 100% seguro sozinho: duas requisiÃ§Ãµes podem passar por
        // aqui ao mesmo tempo (antes de qualquer uma ter dado commit) e as
        // duas verem a lista vazia. Ã‰ sÃ³ pra dar um erro rÃ¡pido e claro (409)
        // no caso comum, sem precisar esperar o banco rejeitar.
        List<Appointment> conflitos = repository.findActiveByVetAndSlot(vet.getId(), dto.scheduledAt());
        if (!conflitos.isEmpty()) {
            throw new AppointmentConflictException(
                    "VeterinÃ¡rio jÃ¡ possui um agendamento ativo nesse horÃ¡rio.");
        }

        Appointment appointment = new Appointment(pet, vet, dto.scheduledAt(), dto.notes());
        // O índice único no PostgreSQL é a garantia final contra concorrência.
        return repository.save(appointment);
    }

    public Appointment findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento nÃ£o encontrado: id=" + id));
    }

    public List<Appointment> findAll() {
        return repository.findAll();
    }

    public List<Appointment> findByVeterinarian(Long vetId) {
        return repository.findByVeterinarianId(vetId);
    }

    @Transactional
    public Appointment cancel(Long id) {
        Appointment appointment = findById(id);
        appointment.cancel();
        return appointment; // dirty checking cuida do UPDATE
    }

    @Transactional
    public Appointment confirm(Long id) {
        Appointment appointment = findById(id);
        appointment.confirm();
        return appointment;
    }
}

