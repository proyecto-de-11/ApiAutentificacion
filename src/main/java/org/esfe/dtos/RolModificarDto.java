package org.esfe.dtos;

import java.io.Serializable;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RolModificarDto implements Serializable {
    @NotNull(message = "El ID del rol es obligatorio para la modificación.")
    @Min(value = 1, message = "El ID debe ser un valor positivo.")
    private Long id;

    @NotNull(message = "El nombre no puede ser nulo.")
    @NotBlank(message = "El nombre es obligatorio.")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres.")
    private String nombre;

    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres.")
    private String descripcion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}