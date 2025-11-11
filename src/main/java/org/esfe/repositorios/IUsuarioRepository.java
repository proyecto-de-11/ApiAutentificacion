package org.esfe.repositorios;

import org.esfe.modelos.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByEmailIgnoreCase(String email);

    Page<Usuario> findByEmailContainingIgnoreCase(String email, Pageable pageable);

    Page<Usuario> findByEstaActivo(Boolean estaActivo, Pageable pageable);

    Page<Usuario> findByEmailContainingIgnoreCaseOrEstaActivo(String email, Boolean estaActivo, Pageable pageable);
}

