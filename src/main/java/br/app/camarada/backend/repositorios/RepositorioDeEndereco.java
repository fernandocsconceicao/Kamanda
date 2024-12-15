package br.app.camarada.backend.repositorios;


import br.app.camarada.backend.entidades.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioDeEndereco extends JpaRepository<Endereco, Long> {
    @Query(nativeQuery = true, value = "Select * from endereco where cliente_id = :idCliente  ")
    Endereco obterPorCliente(@Param("idCliente") Long idCliente);
    @Query(nativeQuery = true, value = "Select * from endereco where estabelecimento_id = :value  ")
    Endereco obterPorEstabelecimento(@Param("value") Long value);
}
