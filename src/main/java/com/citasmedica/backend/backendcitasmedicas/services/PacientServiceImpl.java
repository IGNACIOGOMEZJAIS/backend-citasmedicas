package com.citasmedica.backend.backendcitasmedicas.services;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.citasmedica.backend.backendcitasmedicas.models.entities.Pacient;
import com.citasmedica.backend.backendcitasmedicas.repositories.PacientRepository;



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

    @Override
    @Transactional
    public Pacient save(Pacient pacient) {
        return pacientRepository.save(pacient);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        pacientRepository.deleteById(id);
    }

}
