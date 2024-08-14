package com.citasmedica.backend.backendcitasmedicas.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;
import com.citasmedica.backend.backendcitasmedicas.models.entities.Cita;
import com.citasmedica.backend.backendcitasmedicas.models.entities.Pacient;
import com.citasmedica.backend.backendcitasmedicas.services.CitasService;
import com.citasmedica.backend.backendcitasmedicas.services.EmailService;
import com.citasmedica.backend.backendcitasmedicas.services.PacientService;

@RestController
@RequestMapping("/citas")
@CrossOrigin(originPatterns = "*")
public class CitaController {

    @Autowired
    private CitasService service;

    @Autowired
    private PacientService pacientService;

    @Autowired
    private EmailService emailService;

    @GetMapping
    public List<Cita> list() {
        return service.findAll();
    }

    @GetMapping("/{idCita}")
    public ResponseEntity<?> show(@PathVariable Long idCita) {
        Optional<Cita> CitaOptional = service.findById(idCita);
        if (CitaOptional.isPresent()) {
            return ResponseEntity.ok(CitaOptional.orElseThrow());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Cita cita, HttpServletRequest request) {
        String nombreApe = (String) request.getAttribute("nombreApe");
        String dniStr = (String) request.getAttribute("dni");
        Long dni;
        dni = Long.parseLong(dniStr);
        Optional<Pacient> pacientOpt = pacientService.findByDni(dni);
        if (!pacientOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Paciente no encontrado");
        }
        Pacient pacient = pacientOpt.get();

        cita.setEstado("En horario");
        cita.setRecetaMedica(null);
        cita.setPacient(nombreApe);
        cita.setIdPaciente(pacient.getId());

        // Guardar la cita en la base de datos
        Cita savedCita = service.save(cita);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCita);
    }

    @PutMapping("/{idCita}")
    public ResponseEntity<?> update(@RequestBody Cita cita, @PathVariable Long idCita) {
        Optional<Cita> o = service.findById(idCita);
        if (o.isPresent()) {
            Cita citaDb = o.orElseThrow();
            citaDb.setFecha(cita.getFecha());
            citaDb.setHora(cita.getHora());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.save(citaDb));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/citacurso/updateReceta/{idCita}")
    public ResponseEntity<?> updateReceta(@RequestBody Cita cita, @PathVariable Long idCita) {
        Optional<Cita> o = service.findById(idCita);

        if (o.isPresent()) {
            Cita citaDb = o.orElseThrow();
            citaDb.setRecetaMedica(cita.getRecetaMedica());
            service.save(citaDb);
            Optional<Pacient> pacientOpt = pacientService.findById(citaDb.getIdPaciente());
            if (!pacientOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Paciente no encontrado");
            }
            Pacient pacient = pacientOpt.get();

            try {
                String subject = "Receta Médica";
                String firma = "\n\n" +
                        "Dr: Ernesto Arturo Gomez\n" +
                        "\n" +
                        "MD: Otorrinolaringólogo\n" +
                        "MN: 3142\n";

                String body = "Estimado/a " + pacient.getNombreApe() + ",\n\n" + "DNI:" + pacient.getDni()+","+
                        "\n\n" +
                        "Detalles de Receta:\n " +
                        cita.getRecetaMedica() +"."+ "\n"
                        + "\n\n" + "Fecha:" + "\n" +
                        citaDb.getFecha()+"." + "\n\n" +
                        "\n" +
                        firma;
                emailService.sendEmail(pacient.getEmail(), subject, body);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al enviar el correo electrónico");
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(citaDb);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{idCita}")
    public ResponseEntity<?> remove(@PathVariable Long idCita) {
        Optional<Cita> o = service.findById(idCita);
        if (o.isPresent()) {
            service.remove(idCita);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/citacurso/{idCita}")
    public ResponseEntity<?> sumarTardanzasDeTurno(@RequestBody Cita cita, @PathVariable Long idCita) {
        Optional<Cita> o = service.sumarTardanzasDeTurno(cita, idCita);
        if (o.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(o.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/fecha/{fecha}/{medico}/{especialidad}")
    public ResponseEntity<?> findByFecha(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @PathVariable String medico, @PathVariable String especialidad) {

        List<Cita> citas = new ArrayList<>();
        for (Cita cita : service.findAll()) {
            if (cita.getFecha().equals(fecha) && cita.getMedico().equals(medico)
                    && cita.getEspecialidad().equals(especialidad)) {
                citas.add(cita);
            }
        }

        if (!citas.isEmpty()) {
            return ResponseEntity.ok(citas);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/citafinish/{idCita}")
    public ResponseEntity<?> finishCita(@PathVariable Long idCita) {
        Optional<Cita> o = service.findById(idCita);
        if (o.isPresent()) {
            Cita citaDb = o.orElseThrow();
            citaDb.setEstado("Terminado");
            return ResponseEntity.status(HttpStatus.CREATED).body(service.save(citaDb));
        }
        return ResponseEntity.notFound().build();
    }
}
