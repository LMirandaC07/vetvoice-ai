package com.vetvoice.appointment.dto;

import com.vetvoice.appointment.Appointment;
import com.vetvoice.appointment.AppointmentStatus;
import java.time.LocalDateTime;

public record AppointmentResponseDTO(
        Long id,
        Long petId,
        String petName,
        Long veterinarianId,
        String veterinarianName,
        LocalDateTime scheduledAt,
        AppointmentStatus status,
        String notes
) {
    public static AppointmentResponseDTO from(Appointment a) {
        return new AppointmentResponseDTO(
                a.getId(),
                a.getPet().getId(),
                a.getPet().getName(),
                a.getVeterinarian().getId(),
                a.getVeterinarian().getName(),
                a.getScheduledAt(),
                a.getStatus(),
                a.getNotes()
        );
    }
}
