package org.esfe.repositorios;

import org.esfe.modelos.UsuarioAceptacionTermino;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUsuarioAceptacionTerminoRepository extends JpaRepository<UsuarioAceptacionTermino, Integer> {

    List<UsuarioAceptacionTermino> findByUsuario_Id(Integer idUsuario);

    List<UsuarioAceptacionTermino> findByDocumentoLegal_Id(Integer idDocumentoLegal);

    Page<UsuarioAceptacionTermino> findByUsuario_Id(Integer idUsuario, Pageable pageable);

    Page<UsuarioAceptacionTermino> findByDocumentoLegal_Id(Integer idDocumentoLegal, Pageable pageable);

}

