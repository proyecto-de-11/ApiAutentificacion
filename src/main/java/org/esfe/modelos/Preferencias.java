package org.esfe.modelos;
import jakarta.persistence.*;

@Entity
@Table(name = "preferencias")
public class Preferencias {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "icono", columnDefinition = "TEXT")
    private String icono;

    @Column (name = "esta_activo", nullable = false , columnDefinition = "BOOLEAN")
    private Boolean estaActivo;

    // Getters y Setters

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

    public String getIcono() {
        return icono;
    }
    public void setIcono(String icono) {
        this.icono = icono;
    }
    public Boolean getEstaActivo() {
        return estaActivo;
    }
    public void setEstaActivo(Boolean estaActivo) {
        this.estaActivo = estaActivo;
    }


}
