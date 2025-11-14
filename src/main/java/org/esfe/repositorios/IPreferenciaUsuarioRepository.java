package org.esfe.repositorios;

import org.esfe.modelos.PreferenciaUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IPreferenciaUsuarioRepository extends JpaRepository<PreferenciaUsuario, Integer> {
    Optional<PreferenciaUsuario> findByUsuarioId(Integer usuarioId);
}

