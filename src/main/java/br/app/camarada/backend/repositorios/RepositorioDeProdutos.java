package br.app.camarada.backend.repositorios;

import br.app.camarada.backend.entidades.Produto;
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
                    "WHERE id_regiao = :regiao " +
                    "ORDER BY avaliacao DESC, data_exibicao DESC " +
                    "LIMIT :limite")
    List<Produto> buscarPorExibicaoEAvaliacao(@Param("regiao") Long idDeRegiao, @Param("limite") Integer limite);
}