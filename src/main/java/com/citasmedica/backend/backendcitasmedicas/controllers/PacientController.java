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
import com.citasmedica.backend.backendcitasmedicas.models.entities.Pacient;
import com.citasmedica.backend.backendcitasmedicas.services.PacientService;

@RestController
@RequestMapping("/pacients")
@CrossOrigin(originPatterns = "*")
public class PacientController {
    
   
    
        @Autowired
        private PacientService service;
    
        @GetMapping
        public List<Pacient> list(){
            return service.findAll();
        }
    
        @GetMapping("/{id}")
        public ResponseEntity<?> show(@PathVariable Long id){
            Optional<Pacient> PacientOptional = service.findById(id);
            if(PacientOptional.isPresent()){
                return ResponseEntity.ok(PacientOptional.orElseThrow());
            }
            
            return ResponseEntity.notFound().build();
        }
    
        @PostMapping
        public ResponseEntity<?> create(@RequestBody Pacient pacient){
            return ResponseEntity.status(HttpStatus.CREATED).body(service.save(pacient)); 
        }
    
        @PutMapping("/{id}")
        public ResponseEntity<?> update(@RequestBody Pacient pacient, @PathVariable Long id){
    
            Optional<Pacient> o =  service.findById(id);
            if(o.isPresent()){
             Pacient pacientDb = o.orElseThrow();
             pacientDb.setEmail(pacient.getEmail());
             pacientDb.setDni(pacient.getDni());
                return ResponseEntity.status(HttpStatus.CREATED).body(service.save(pacientDb)); 
    
            }
            return ResponseEntity.notFound().build();
    
        }
        @DeleteMapping("/{id}")
        public ResponseEntity<?> remove(@PathVariable Long id){
            Optional<Pacient> o = service.findById (id);
            if(o.isPresent()){
    
                service.remove(id);
                return ResponseEntity.noContent().build();
    
            }
            return ResponseEntity.notFound().build();
    
        }
    
    }
    

