package com.citasmedica.backend.backendcitasmedicas.services;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.citasmedica.backend.backendcitasmedicas.models.entities.Cita;
import com.citasmedica.backend.backendcitasmedicas.models.entities.Pacient;
import com.citasmedica.backend.backendcitasmedicas.repositories.CitaRepository;

@Service
public class CitaServiceImpl implements CitasService {

    @Autowired
    private CitaRepository repository;

    @Autowired
    private PacientService pacientService;

    @Autowired
    private EmailService emailService;

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

    @Override
    @Transactional
    public Optional<Cita> sumarTardanzasDeTurno(Cita cita, Long idCita) {
        Optional<Cita> o = this.findById(idCita);
        Cita citaOptional = null;
        if (o.isPresent()) {
            Cita citaDb = o.orElseThrow();
            Long tardanzasTotales = 0L;
            tardanzasTotales = tardanzasTotales + cita.getTardanza();
            LocalDate fechaCita = o.get().getFecha();
            String medicoCita = o.get().getMedico();
            String especialidadCita = o.get().getEspecialidad();

            List<Cita> citasDelTurno = (List<Cita>) repository.findByFecha(fechaCita, medicoCita, especialidadCita);

            // Calcular la suma de todas las tardanzas
            for (Cita c : citasDelTurno) {
                c.setEstado("Terminado");
                if (!c.getIdCita().equals(idCita)) {
                    c.setHora(c.getHora().plusMinutes(tardanzasTotales));
                    c.setEstado("Retrasado");
                    citaOptional = this.save(c);
                    Optional<Pacient> pacientOpt = pacientService.findById(c.getIdPaciente());
                    if (!pacientOpt.isPresent()) {
                        return Optional.empty();
                    }
                    Pacient pacient = pacientOpt.get();
                    try {
                        String subject = "Turno Retrasado";

                        String body = "Estimado/a " + pacient.getNombreApe() + ",\n\n" +
                                "DNI: " + pacient.getDni() + "\n\n" +

                                "Su turno ha sido retrasado para las " + c.getHora() + " horas.\n\n" +

                                "Lamentamos las molestias que esto pueda causarle.\n\n" +
                                "Atentamente,\n" +

                                "El equipo de Citas Médicas.";

                        emailService.sendEmail(pacient.getEmail(), subject, body);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return Optional.empty();
                    }
                }
            }

        }
        return Optional.ofNullable(citaOptional);
    }

    @Override
    @Transactional
    public List<Cita> findByFecha(LocalDate fecha, String medico, String especialidad) {
        return repository.findByFecha(fecha, medico, especialidad);
    }

}