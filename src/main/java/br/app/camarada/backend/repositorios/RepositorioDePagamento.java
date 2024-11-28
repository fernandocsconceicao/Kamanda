package br.app.camarada.backend.repositorios;


import br.app.camarada.backend.entidades.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RepositorioDePagamento extends JpaRepository<Pagamento, Long> {

    List<Pagamento> findByUsuarioIdOrderByHoraDoPedidoDesc(Long usuarioId);

    Pagamento findByPixId(Long codigo);
}
