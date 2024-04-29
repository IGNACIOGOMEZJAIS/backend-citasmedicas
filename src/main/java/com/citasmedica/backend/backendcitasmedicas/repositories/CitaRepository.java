package com.citasmedica.backend.backendcitasmedicas.repositories;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.citasmedica.backend.backendcitasmedicas.models.entities.Cita;

public interface CitaRepository extends org.springframework.data.repository.CrudRepository<Cita,Long>{
    @Query("SELECT c FROM Cita c WHERE c.fecha = :fecha AND c.medico = :medico AND c.especialidad = :especialidad AND c.estado != 'Terminado'")
    List<Cita> findByFecha(@Param("fecha") LocalDate fecha, @Param("medico") String medico, @Param("especialidad") String especialidad);
}