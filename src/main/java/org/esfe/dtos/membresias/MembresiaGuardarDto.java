package org.esfe.dtos.membresias;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class MembresiaGuardarDto {

    @NotBlank(message = "El nombre es requerido")
    private String nombre;

    @NotBlank(message = "La descripción es requerida")
    private String descripcion;

    @NotNull(message = "El precio mensual es requerido")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio mensual debe ser mayor que 0")
    private BigDecimal precioMensual;

    // Beneficios como JSON en texto
    private String beneficios;

    @NotNull(message = "El máximo de reservas por mes es requerido")
    private Integer maxReservasMes;

    @NotNull(message = "El descuento en porcentaje es requerido")
    @Min(value = 0, message = "El descuento no puede ser negativo")
    @Max(value = 100, message = "El descuento no puede ser mayor a 100")
    private Integer descuentoPorcentaje;

    @NotNull(message = "El estado activo es requerido")
    private Boolean estaActivo;

    // Getters y Setters
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

    public BigDecimal getPrecioMensual() {
        return precioMensual;
    }

    public void setPrecioMensual(BigDecimal precioMensual) {
        this.precioMensual = precioMensual;
    }

    public String getBeneficios() {
        return beneficios;
    }

    public void setBeneficios(String beneficios) {
        this.beneficios = beneficios;
    }

    public Integer getMaxReservasMes() {
        return maxReservasMes;
    }

    public void setMaxReservasMes(Integer maxReservasMes) {
        this.maxReservasMes = maxReservasMes;
    }

    public Integer getDescuentoPorcentaje() {
        return descuentoPorcentaje;
    }

    public void setDescuentoPorcentaje(Integer descuentoPorcentaje) {
        this.descuentoPorcentaje = descuentoPorcentaje;
    }

    public Boolean getEstaActivo() {
        return estaActivo;
    }

    public void setEstaActivo(Boolean estaActivo) {
        this.estaActivo = estaActivo;
    }
}
