package org.esfe.repositorios;

import org.esfe.modelos.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {

    /**
     * Buscar usuario por email (para autenticación JWT)
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Buscar usuario por email ignorando mayúsculas
     */
    Optional<Usuario> findByEmailIgnoreCase(String email);

    /**
     * Buscar usuarios por email con paginación
     */
    Page<Usuario> findByEmailContainingIgnoreCase(String email, Pageable pageable);

    /**
     * Buscar usuarios por estado activo
     */
    Page<Usuario> findByEstaActivo(Boolean estaActivo, Pageable pageable);

    /**
     * Buscar usuarios por email o estado activo
     */
    Page<Usuario> findByEmailContainingIgnoreCaseOrEstaActivo(String email, Boolean estaActivo, Pageable pageable);
}