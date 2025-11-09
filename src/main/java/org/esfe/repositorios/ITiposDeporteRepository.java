package org.esfe.repositorios;

import org.esfe.modelos.TipoDeporte;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ITiposDeporteRepository extends JpaRepository<TipoDeporte, Long> {

    Optional<TipoDeporte> findByNombreIgnoreCase(String nombre);

    Page<TipoDeporte> findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(String nombre, String descripcion, Pageable pageable);

    Page<TipoDeporte> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);

    Page<TipoDeporte> findByDescripcionContainingIgnoreCase(String descripcion, Pageable pageable);
}

