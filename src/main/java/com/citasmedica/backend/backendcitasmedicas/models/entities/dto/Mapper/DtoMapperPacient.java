package com.citasmedica.backend.backendcitasmedicas.models.entities.dto.Mapper;

import com.citasmedica.backend.backendcitasmedicas.models.entities.Pacient;
import com.citasmedica.backend.backendcitasmedicas.models.entities.dto.PacientDto;

public class DtoMapperPacient {
    private static DtoMapperPacient mapper;
    private Pacient pacient;
    private  DtoMapperPacient(){

    }
    public static DtoMapperPacient builder(){
        mapper = new DtoMapperPacient();

        return mapper;

    }
    public DtoMapperPacient setPacient(Pacient pacient) {
        this.pacient = pacient;
        return mapper;
    }
    public PacientDto build (){
        if(pacient == null){
            throw new RuntimeException("Debe pasar el entity pacient");

        }
        
        return  new PacientDto(this.pacient.getId(), this.pacient.getDni(), this.pacient.getEmail(), this.pacient.getNombreApe());
        
    }
    

}
