package com.citasmedica.backend.backendcitasmedicas.services;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.citasmedica.backend.backendcitasmedicas.models.entities.Cita;

public interface CitasService {
List<Cita> findAll();
Optional<Cita> findById(Long idCita);
Cita save(Cita cita);
void remove(Long idCita);
Optional<Cita>sumarTardanzasDeTurno(Cita cita,Long idCita);
List<Cita> findByFecha(LocalDate fecha, String medico, String especialidad);



}
