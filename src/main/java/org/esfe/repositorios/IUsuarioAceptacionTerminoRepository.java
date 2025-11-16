package org.esfe.repositorios;

import org.esfe.modelos.UsuarioAceptacionTermino;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUsuarioAceptacionTerminoRepository extends JpaRepository<UsuarioAceptacionTermino, Integer> {

    Optional<UsuarioAceptacionTermino> findByUsuario_Id(Integer idUsuario);

    List<UsuarioAceptacionTermino> findByDocumentosAceptados_DocumentoLegal_Id(Integer idDocumentoLegal);

    Page<UsuarioAceptacionTermino> findByUsuario_Id(Integer idUsuario, Pageable pageable);

    Page<UsuarioAceptacionTermino> findByDocumentosAceptados_DocumentoLegal_Id(Integer idDocumentoLegal, Pageable pageable);

}