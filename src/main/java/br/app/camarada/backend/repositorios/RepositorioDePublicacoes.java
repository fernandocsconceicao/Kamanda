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

    @Query(nativeQuery = true, value = " Select * from publicacao where status_propaganda=:value ")
    List<Publicacao> obterPorStatus(@Param(value = "value") Integer statusPropaganda);
    @Query(nativeQuery = true, value = " Select * from publicacao where categoria_publicacao = :valor ")
    List<Publicacao> obterPorCategoria(@Param("valor") Integer categoria);
    @Query(nativeQuery = true, value = " Select * from publicacao LIMIT :limite")
    List<Publicacao> obterPublicacoesSemCategoria(@Param("limite") Integer limite );
}
