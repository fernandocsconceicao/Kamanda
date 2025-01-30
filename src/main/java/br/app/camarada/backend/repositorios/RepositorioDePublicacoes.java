package br.app.camarada.backend.repositorios;

import br.app.camarada.backend.entidades.Publicacao;
import br.app.camarada.backend.enums.CategoriaPublicacao;
import br.app.camarada.backend.enums.StatusPropaganda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioDePublicacoes extends JpaRepository<Publicacao,Long> {

    List<Publicacao> findByIdPerfil(Long id);


    @Query(nativeQuery = true, value = "SELECT * FROM publicacao WHERE status_propaganda=:value ORDER BY id DESC")
    List<Publicacao> obterPorStatus(@Param(value = "value") Integer statusPropaganda);

    @Query(nativeQuery = true, value = "SELECT * FROM publicacao WHERE categoria_publicacao = :valor ORDER BY id DESC")
    List<Publicacao> obterPorCategoria(@Param("valor") Integer categoria);

    @Query(nativeQuery = true, value = "SELECT * FROM publicacao ORDER BY id DESC LIMIT :limite")
    List<Publicacao> obterPublicacoesSemCategoria(@Param("limite") Integer limite);
}
