package org.esfe.dtos;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RolSalidaDto implements Serializable{
    private Long id;

    private String nombre;
    
    private String descripcion;
}