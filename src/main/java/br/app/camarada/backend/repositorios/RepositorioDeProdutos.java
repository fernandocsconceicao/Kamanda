package br.app.camarada.backend.repositorios;

import br.app.camarada.backend.entidades.Produto;
import br.app.camarada.backend.enums.CategoriaProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioDeProdutos extends JpaRepository<Produto, Long> {

    @Query(nativeQuery = true, value = "Select * from produto where estabelecimento_id = :value")
    List<Produto> findByEstablishmentId(@Param("value") Long establishmentId);


    @Query(nativeQuery = true,
            value = "SELECT * FROM produto " +
                    "ORDER BY avaliacao DESC, data_exibicao DESC " +
                    "LIMIT :limite")
    List<Produto> buscarPorExibicaoEAvaliacao( @Param("limite") Integer limite);

    @Query(nativeQuery = true,
            value = "SELECT * FROM produto " +
                    "where categoria_produto= :valor ORDER BY avaliacao DESC, data_exibicao DESC " +
                    "LIMIT :limite")
    List<Produto> buscarPorCategoria(@Param("valor") Integer categoriaProduto, @Param("limite") Integer limite);
}