package com.citasmedica.backend.backendcitasmedicas.repositories;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

import com.citasmedica.backend.backendcitasmedicas.models.entities.Role;

public interface RoleRepository extends CrudRepository<Role,Long>
{
    Optional<Role> findByName(String name);

  

}
