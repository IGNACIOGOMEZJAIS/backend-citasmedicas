package com.citasmedica.backend.backendcitasmedicas.repositories;
import java.time.LocalDate;
import java.util.List;

import com.citasmedica.backend.backendcitasmedicas.models.entities.Cita;

public interface CitaRepository extends org.springframework.data.repository.CrudRepository<Cita,Long>{
    List<Cita> findByFecha(LocalDate fecha);
}
