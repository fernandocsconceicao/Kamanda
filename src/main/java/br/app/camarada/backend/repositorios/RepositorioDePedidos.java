package br.app.camarada.backend.repositorios;


import br.app.camarada.backend.entidades.Pedido;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioDePedidos extends JpaRepository<Pedido, Long> {

    List<Pedido> findAllByCpf(String cpf);

    List<Pedido> findByUsuario(Long header);

    List<Pedido> findByRegiaoId(Long regiaoId);

}
