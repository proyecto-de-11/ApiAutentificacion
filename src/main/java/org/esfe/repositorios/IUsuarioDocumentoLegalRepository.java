package org.esfe.repositorios;

import org.esfe.modelos.UsuarioDocumentoLegal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUsuarioDocumentoLegalRepository extends JpaRepository<UsuarioDocumentoLegal, Integer> {
}