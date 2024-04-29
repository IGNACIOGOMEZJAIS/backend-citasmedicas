package com.citasmedica.backend.backendcitasmedicas.models.entities;
import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="citas")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCita;
    private String medico;
    private LocalDate fecha;
    private String especialidad;
    private LocalTime hora;
    private String estado;
    private String recetaMedica;
    private String pacient;
    private Long idPaciente;
    private Long tardanza;

    public Long getIdCita() {
        return idCita;
    }
    public void setIdCita(Long idCita) {
        this.idCita = idCita;
    }
    public String getMedico() {
        return medico;
    }
    public void setMedico(String medico) {
        this.medico = medico;
    }
    

    public LocalTime getHora() {
        return hora;
    }
    public void setHora(LocalTime hora) {
        this.hora = hora;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getRecetaMedica() {
        return recetaMedica;
    }
    public void setRecetaMedica(String recetaMedica) {
        this.recetaMedica = recetaMedica;
    }
    public String getPacient() {
        return pacient;
    }
    public void setPacient(String pacient) {
        this.pacient = pacient;
    }
    public String getEspecialidad() {
        return especialidad;
    }
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
    public Long getIdPaciente() {
        return idPaciente;
    }
    public void setIdPaciente(Long idPaciente) {
        this.idPaciente = idPaciente;
    }
    public Long getTardanza() {
        return tardanza;
    }
    public void setTardanza(Long tardanza) {
        this.tardanza = tardanza;
    }
    public LocalDate getFecha() {
        return fecha;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
 
  
   
    


    
    




}
