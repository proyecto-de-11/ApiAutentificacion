package org.esfe.modelos;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "preferencia_usuario")
public class PreferenciaUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Mantener OneToOne con Usuario
    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", unique = true)
    private Usuario usuario;

    // CAMBIO: Guardar los IDs como JSON/TEXT
    @Column(name = "tipo_deporte_id", columnDefinition = "JSON")
    private String tipoDeporteIds; // Guardará: "[1,2,4,6]"

    @Column(name = "nivel_juego")
    private String nivelJuego;

    @Column(name = "posicion_preferida")
    private String posicionPreferida;

    @Column(name = "horario_preferido_inicio")
    private LocalTime horarioPreferidoInicio;

    @Column(name = "horario_preferido_fin")
    private LocalTime horarioPreferidoFin;

    @Column(name = "dias_preferidos", columnDefinition = "text")
    private String diasPreferidos;

    @Column(name = "ciudad_preferida")
    private String ciudadPreferida;

    @Column(name = "radios_distancia_km")
    private Integer radiosDistanciaKm;

    @Column(name = "notificaciones_email")
    private Boolean notificacionesEmail;

    @Column(name = "notificaciones_push")
    private Boolean notificacionesPush;

    @Column(name = "notificaciones_partidos")
    private Boolean notificacionesPartidos;

    @Column(name = "notificaciones_torneos")
    private Boolean notificacionesTorneos;

    @Column(name = "notificaciones_invitaciones")
    private Boolean notificacionesInvitaciones;

    @Column(name = "fecha_guardado")
    private LocalDateTime fechaGuardado;

    @Column(name = "fecha_actualizado")
    private LocalDateTime fechaActualizado;

    @PrePersist
    public void prePersist() {
        this.fechaGuardado = LocalDateTime.now();
        this.fechaActualizado = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaActualizado = LocalDateTime.now();
    }

    // Getters y setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTipoDeporteIds() {
        return tipoDeporteIds;
    }

    public void setTipoDeporteIds(String tipoDeporteIds) {
        this.tipoDeporteIds = tipoDeporteIds;
    }

    public String getNivelJuego() {
        return nivelJuego;
    }

    public void setNivelJuego(String nivelJuego) {
        this.nivelJuego = nivelJuego;
    }

    public String getPosicionPreferida() {
        return posicionPreferida;
    }

    public void setPosicionPreferida(String posicionPreferida) {
        this.posicionPreferida = posicionPreferida;
    }

    public LocalTime getHorarioPreferidoInicio() {
        return horarioPreferidoInicio;
    }

    public void setHorarioPreferidoInicio(LocalTime horarioPreferidoInicio) {
        this.horarioPreferidoInicio = horarioPreferidoInicio;
    }

    public LocalTime getHorarioPreferidoFin() {
        return horarioPreferidoFin;
    }

    public void setHorarioPreferidoFin(LocalTime horarioPreferidoFin) {
        this.horarioPreferidoFin = horarioPreferidoFin;
    }

    public String getDiasPreferidos() {
        return diasPreferidos;
    }

    public void setDiasPreferidos(String diasPreferidos) {
        this.diasPreferidos = diasPreferidos;
    }

    public String getCiudadPreferida() {
        return ciudadPreferida;
    }

    public void setCiudadPreferida(String ciudadPreferida) {
        this.ciudadPreferida = ciudadPreferida;
    }

    public Integer getRadiosDistanciaKm() {
        return radiosDistanciaKm;
    }

    public void setRadiosDistanciaKm(Integer radiosDistanciaKm) {
        this.radiosDistanciaKm = radiosDistanciaKm;
    }

    public Boolean getNotificacionesEmail() {
        return notificacionesEmail;
    }

    public void setNotificacionesEmail(Boolean notificacionesEmail) {
        this.notificacionesEmail = notificacionesEmail;
    }

    public Boolean getNotificacionesPush() {
        return notificacionesPush;
    }

    public void setNotificacionesPush(Boolean notificacionesPush) {
        this.notificacionesPush = notificacionesPush;
    }

    public Boolean getNotificacionesPartidos() {
        return notificacionesPartidos;
    }

    public void setNotificacionesPartidos(Boolean notificacionesPartidos) {
        this.notificacionesPartidos = notificacionesPartidos;
    }

    public Boolean getNotificacionesTorneos() {
        return notificacionesTorneos;
    }

    public void setNotificacionesTorneos(Boolean notificacionesTorneos) {
        this.notificacionesTorneos = notificacionesTorneos;
    }

    public Boolean getNotificacionesInvitaciones() {
        return notificacionesInvitaciones;
    }

    public void setNotificacionesInvitaciones(Boolean notificacionesInvitaciones) {
        this.notificacionesInvitaciones = notificacionesInvitaciones;
    }

    public LocalDateTime getFechaGuardado() {
        return fechaGuardado;
    }

    public void setFechaGuardado(LocalDateTime fechaGuardado) {
        this.fechaGuardado = fechaGuardado;
    }

    public LocalDateTime getFechaActualizado() {
        return fechaActualizado;
    }

    public void setFechaActualizado(LocalDateTime fechaActualizado) {
        this.fechaActualizado = fechaActualizado;
    }
}