package com.citasmedica.backend.backendcitasmedicas.repositories;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.citasmedica.backend.backendcitasmedicas.models.entities.Pacient;

public interface PacientRepository extends CrudRepository<Pacient,Long>
{
    Optional<Pacient> findByDni(Long dni);

    @Query("select u from Pacient u where u.dni=?1")
    Optional<Pacient> getPacientByDni(Long dni);

    

}
