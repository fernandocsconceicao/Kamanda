package br.app.camarada.backend.repositorios;

import br.app.camarada.backend.entidades.EntregaEmFila;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RepositorioFilaDeEntregas extends JpaRepository<EntregaEmFila, Long> {

    @Query(nativeQuery = true, value = "Select * from entrega_em_fila where id_pedido = :idPedido ")
    EntregaEmFila obterPorIdPedido(

            @Param("idPedido") Long idPedido
    );

    @Query(nativeQuery = true, value = "Select * from entrega_em_fila where id_estabelecimento = :idEstabelecimento order by hora_chegada DESC")
    List<EntregaEmFila> obterPorEstabelecimento(
            @Param("idEstabelecimento") Long idEstabelecimento
    );
}
