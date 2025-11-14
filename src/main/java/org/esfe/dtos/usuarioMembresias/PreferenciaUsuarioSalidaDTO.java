package org.esfe.dtos.usuarioMembresias;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class PreferenciaUsuarioSalidaDTO {
    private Integer id;
    private Integer usuarioId;
    private Long tipoDeporteId;
    private String nivelJuego;
    private String posicionPreferida;
    private LocalTime horarioPreferidoInicio;
    private LocalTime horarioPreferidoFin;
    private String diasPreferidos; // JSON string
    private String ciudadPreferida;
    private Integer radiosDistanciaKm;
    private Boolean notificacionesEmail;
    private Boolean notificacionesPush;
    private Boolean notificacionesPartidos;
    private Boolean notificacionesTorneos;
    private Boolean notificacionesInvitaciones;
    private LocalDateTime fechaGuardado;
    private LocalDateTime fechaActualizado;

    // Getters y setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getTipoDeporteId() {
        return tipoDeporteId;
    }

    public void setTipoDeporteId(Long tipoDeporteId) {
        this.tipoDeporteId = tipoDeporteId;
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
