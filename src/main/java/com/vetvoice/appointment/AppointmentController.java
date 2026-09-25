package com.vetvoice.appointment;

import com.vetvoice.appointment.dto.AppointmentRequestDTO;
import com.vetvoice.appointment.dto.AppointmentResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService service;

    public AppointmentController(AppointmentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> create(@Valid @RequestBody AppointmentRequestDTO dto) {
        Appointment saved = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(AppointmentResponseDTO.from(saved));
    }

    @GetMapping("/{id}")
    public AppointmentResponseDTO findById(@PathVariable Long id) {
        return AppointmentResponseDTO.from(service.findById(id));
    }

    @GetMapping
    public List<AppointmentResponseDTO> findAll(@RequestParam(required = false) Long veterinarianId) {
        List<Appointment> appointments = veterinarianId != null
                ? service.findByVeterinarian(veterinarianId)
                : service.findAll();
        return appointments.stream().map(AppointmentResponseDTO::from).toList();
    }

    @PatchMapping("/{id}/cancel")
    public AppointmentResponseDTO cancel(@PathVariable Long id) {
        return AppointmentResponseDTO.from(service.cancel(id));
    }

    @PatchMapping("/{id}/confirm")
    public AppointmentResponseDTO confirm(@PathVariable Long id) {
        return AppointmentResponseDTO.from(service.confirm(id));
    }
}
