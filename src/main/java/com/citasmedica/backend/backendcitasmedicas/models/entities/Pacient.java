package com.citasmedica.backend.backendcitasmedicas.models.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="pacients")
public class Pacient {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column(unique = true)
private Long dni;
private String password;
private String nombreApe;
@Column(unique=true)
private String email;

@OneToMany(mappedBy = "idPaciente") // Relación uno a muchos con Cita
private List<Cita> citas;

public Long getId() {
    return id;
}

public Pacient() {
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

public String getPassword() {
    return password;
}

public void setPassword(String password) {
    this.password = password;
}

public String getEmail() {
    return email;
}

public void setEmail(String email) {
    this.email = email;
}

public List<Cita> getCitas() {
    return citas;
}

public void setCitas(List<Cita> citas) {
    this.citas = citas;
}

public String getNombreApe() {
    return nombreApe;
}

public void setNombreApe(String nombreApe) {
    this.nombreApe = nombreApe;
}



}
