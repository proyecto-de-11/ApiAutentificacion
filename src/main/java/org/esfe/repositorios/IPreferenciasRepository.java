package org.esfe.repositorios;

import org.esfe.modelos.Preferencias;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IPreferenciasRepository extends JpaRepository<Preferencias, Long> {

    Optional<Preferencias> findByNombreIgnoreCase(String nombre);

    Page<Preferencias> findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(String nombre, String descripcion, Pageable pageable);

    Page<Preferencias> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);

    Page<Preferencias> findByDescripcionContainingIgnoreCase(String descripcion, Pageable pageable);
}

