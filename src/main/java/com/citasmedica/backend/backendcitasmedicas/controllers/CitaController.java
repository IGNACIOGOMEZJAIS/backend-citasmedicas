package com.citasmedica.backend.backendcitasmedicas.controllers;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.citasmedica.backend.backendcitasmedicas.models.entities.Cita;
import com.citasmedica.backend.backendcitasmedicas.services.CitasService;



@RestController
@RequestMapping("/citas")
@CrossOrigin(originPatterns = "*")
public class CitaController {

    @Autowired
    private CitasService service;

    @GetMapping
    public List<Cita> list(){
        return service.findAll();
    }

    @GetMapping("/{idCita}")
    public ResponseEntity<?> show(@PathVariable Long idCita){
        Optional<Cita> CitaOptional = service.findById(idCita);
        if(CitaOptional.isPresent()){
            return ResponseEntity.ok(CitaOptional.orElseThrow());
        }
        
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Cita cita){
        cita.setEstado("En horario");
        cita.setRecetaMedica("paracetamol");
        cita.setPacient("Ignacio Gomez Jais");
        
        // Guardar la cita en la base de datos
        Cita savedCita = service.save(cita);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(savedCita)); 
    }

    @PutMapping("/{idCita}")
    public ResponseEntity<?> update(@RequestBody Cita cita, @PathVariable Long idCita){

        Optional<Cita> o = service.findById(idCita);
        if(o.isPresent()){
            Cita citaDb = o.orElseThrow();
            citaDb.setFecha(cita.getFecha());
            citaDb.setHora(cita.getHora());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.save(citaDb)); 

        }
        return ResponseEntity.notFound().build();

    }
    @DeleteMapping("/{idCita}")
    public ResponseEntity<?> remove(@PathVariable Long idCita){
        Optional<Cita> o = service.findById(idCita);
        if(o.isPresent()){

            service.remove(idCita);
            return ResponseEntity.noContent().build();

        }
        return ResponseEntity.notFound().build();

    }

}
