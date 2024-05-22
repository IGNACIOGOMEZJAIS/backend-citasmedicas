package com.citasmedica.backend.backendcitasmedicas.models.entities.dto;

public class PacientDto {
    private Long id;
    private Long dni;
    private String nombreApe;
    private String email;

    public PacientDto(Long id, Long dni,String nombreApe, String email) {
        this.id = id;
        this.dni = dni;
        this.nombreApe = nombreApe;
        this.email = email;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getDni() {
        return dni;
    }
    public void setDni(Long dni) {
        this.dni = dni;
    }
   
    public String getNombreApe() {
        return nombreApe;
    }
    public void setNombreApe(String nombreApe) {
        this.nombreApe = nombreApe;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public PacientDto() {
    }
    

}
