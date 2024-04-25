package com.citasmedica.backend.backendcitasmedicas.services;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.citasmedica.backend.backendcitasmedicas.models.entities.Cita;
import com.citasmedica.backend.backendcitasmedicas.repositories.CitaRepository;


@Service
public class CitaServiceImpl implements CitasService {

    @Autowired
    private CitaRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Cita> findAll() {
        return (List<Cita>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cita> findById(Long idCita) {
        return repository.findById(idCita);
    }

    @Override
    @Transactional
    public Cita save(Cita cita) {
        return repository.save(cita);
    }
    @Override
    @Transactional
    public void remove(Long idCita) {
        repository.deleteById(idCita);
    }

}
