package org.esfe.modelos;

import jakarta.persistence.*;

@Entity
@Table(name = "preferencia_usuario_tipo_deporte")
public class PreferenciaUsuarioTipoDeporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preferencia_usuario_id", nullable = false)
    private PreferenciaUsuario preferenciaUsuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_deporte_id", nullable = false)
    private TipoDeporte tipoDeporte;

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PreferenciaUsuario getPreferenciaUsuario() {
        return preferenciaUsuario;
    }

    public void setPreferenciaUsuario(PreferenciaUsuario preferenciaUsuario) {
        this.preferenciaUsuario = preferenciaUsuario;
    }

    public TipoDeporte getTipoDeporte() {
        return tipoDeporte;
    }

    public void setTipoDeporte(TipoDeporte tipoDeporte) {
        this.tipoDeporte = tipoDeporte;
    }
}