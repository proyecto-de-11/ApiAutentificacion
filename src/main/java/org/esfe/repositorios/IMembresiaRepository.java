package org.esfe.repositorios;

import org.esfe.modelos.Membresia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IMembresiaRepository extends JpaRepository<Membresia, Integer> {

    List<Membresia> findByNombreContainingIgnoreCase(String texto);

    List<Membresia> findByTituloContainingIgnoreCase(String texto);

    List<Membresia> findByEstaActivo(Boolean estaActivo);

    List<Membresia> findByFechaVigenciaBefore(LocalDate fecha);

    Page<Membresia> findByNombreContainingIgnoreCaseOrTituloContainingIgnoreCase(String nombre, String titulo, Pageable pageable);

    boolean existsByNombreAndVersion(String nombre, String version);
}

