package com.citasmedica.backend.backendcitasmedicas.services;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.citasmedica.backend.backendcitasmedicas.models.entities.Pacient;
import com.citasmedica.backend.backendcitasmedicas.models.entities.Role;
import com.citasmedica.backend.backendcitasmedicas.repositories.PacientRepository;
import com.citasmedica.backend.backendcitasmedicas.repositories.RoleRepository;



@Service
public class PacientServiceImpl implements PacientService {
@Autowired
private PacientRepository pacientRepository;
    @Override
    @Transactional(readOnly = true)
    public List<Pacient> findAll() {
        return (List<Pacient>) pacientRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pacient> findById(Long id) {
     return pacientRepository.findById(id);
    }
     @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;
    @Override
    @Transactional
    public Pacient save(Pacient pacient) {
        String passwordBCrypt = passwordEncoder.encode(pacient.getPassword());
        pacient.setPassword(passwordBCrypt);
        List<Role> roles  = new ArrayList<>();
        Optional<Role> o = roleRepository.findByName("ROLE_PACIENT");
        if(o.isPresent()){
            roles.add(o.orElseThrow());

        }
        pacient.setRoles(roles);

        return pacientRepository.save(pacient);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        pacientRepository.deleteById(id);
    }
    @Override
    @Transactional(readOnly = true)
    public Optional<Pacient> findByDni(Long dni) {
        return pacientRepository.findByDni(dni);
    }

}
