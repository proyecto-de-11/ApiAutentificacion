package org.esfe.repositorios;

import org.esfe.modelos.UsuarioMembresia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUsuarioMembresiaRepository extends JpaRepository<UsuarioMembresia, Integer> {

    List<UsuarioMembresia> findByUsuarioId(Integer usuarioId);

    List<UsuarioMembresia> findByMembresiaId(Integer membresiaId);

    List<UsuarioMembresia> findByEstaActiva(Boolean estaActiva);
}

