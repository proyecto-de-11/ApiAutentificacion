package org.esfe.repositorios;

import org.esfe.modelos.Rol;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IRolRepository extends JpaRepository<Rol, Long> {

    Optional<Rol> findByNombreIgnoreCase(String nombre);

    Page<Rol> findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(String nombre, String descripcion, Pageable pageable);
    
    Page<Rol> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
    
    Page<Rol> findByDescripcionContainingIgnoreCase(String descripcion, Pageable pageable);
}