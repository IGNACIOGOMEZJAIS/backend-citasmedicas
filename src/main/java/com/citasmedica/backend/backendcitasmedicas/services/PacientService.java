package com.citasmedica.backend.backendcitasmedicas.services;

import java.util.List;
import java.util.Optional;

import com.citasmedica.backend.backendcitasmedicas.models.entities.Pacient;

public interface PacientService {

    List<Pacient> findAll();

    Optional<Pacient> findById(Long id);

    Pacient save(Pacient pacient);

    void remove(Long id);
    Optional<Pacient> findByDni(Long dni);
}
