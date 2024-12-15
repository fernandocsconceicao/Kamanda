package br.app.camarada.backend.repositorios;


import br.app.camarada.backend.entidades.Entregador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositorioDaRevolucaoBrasileira extends JpaRepository<Entregador, Long> {


    Optional<Entregador> findByUsuarioId(Long usuarioId);

    @Query(nativeQuery = true,value = "Select * from entregador where status = 3 ")
    List<Entregador> obterEntregadorDisponivel();
}
