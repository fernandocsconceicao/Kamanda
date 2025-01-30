package br.app.camarada.backend.repositorios;


import br.app.camarada.backend.entidades.Propaganda;
import br.app.camarada.backend.entidades.PublicacaoDePropaganda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioDePublicacoesDePropaganda extends JpaRepository<PublicacaoDePropaganda, Long> {


    @Query(nativeQuery = true, value = "SELECT * FROM publicacao_de_propaganda WHERE status_propaganda = 1 LIMIT :limite")
    List<PublicacaoDePropaganda> obterPropagandasParaExibicao(@Param("limite") Integer limite);
}
