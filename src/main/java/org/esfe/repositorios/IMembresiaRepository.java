package org.esfe.repositorios;

import org.esfe.modelos.Membresia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface IMembresiaRepository extends JpaRepository<Membresia, Integer> {

    List<Membresia> findByNombreContainingIgnoreCase(String texto);

    List<Membresia> findByDescripcionContainingIgnoreCase(String texto);

    List<Membresia> findByEstaActivo(Boolean estaActivo);

    Page<Membresia> findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(String nombre, String descripcion, Pageable pageable);

    boolean existsByNombreAndPrecioMensual(String nombre, BigDecimal precioMensual);
}
