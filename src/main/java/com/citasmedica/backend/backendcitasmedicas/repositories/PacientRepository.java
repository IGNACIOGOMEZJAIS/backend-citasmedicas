package com.citasmedica.backend.backendcitasmedicas.repositories;
import org.springframework.data.repository.CrudRepository;
import com.citasmedica.backend.backendcitasmedicas.models.entities.Pacient;

public interface PacientRepository extends CrudRepository<Pacient,Long>
{

}
