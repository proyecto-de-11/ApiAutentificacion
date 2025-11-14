package org.esfe.repositorios;

import org.esfe.modelos.Perfil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IPerfilRepository extends JpaRepository<Perfil, Integer> {

    // Buscar perfil por el id del usuario (accede a la relación usuario.id)
    Optional<Perfil> findByUsuario_Id(Integer usuarioId);

    // Buscar todos los perfiles de una ciudad (case-insensitive)
    List<Perfil> findByCiudadIgnoreCase(String ciudad);

    // Verificar existencia por usuario
    boolean existsByUsuario_Id(Integer usuarioId);

    // Buscar por documento de identidad
    Optional<Perfil> findByDocumentoIdentidad(String documentoIdentidad);

    // Paginado por país
    Page<Perfil> findByPais(String pais, Pageable pageable);

    // Ejemplo de consulta personalizada (JPQL)
    @Query("SELECT p FROM Perfil p WHERE p.pais = :pais AND p.ciudad = :ciudad")
    List<Perfil> buscarPorPaisYCiudad(@Param("pais") String pais, @Param("ciudad") String ciudad);

}
