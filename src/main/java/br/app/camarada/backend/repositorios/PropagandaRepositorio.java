package br.app.camarada.backend.repositorios;


import br.app.camarada.backend.entidades.Caixa;
import br.app.camarada.backend.entidades.Propaganda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropagandaRepositorio extends JpaRepository<Propaganda, Long> {


    @Query(nativeQuery = true, value = "SELECT * FROM PROPAGANDA WHERE status_propaganda = 0 ")
    List<Propaganda> obterPropagandasParaExibicao();
}
