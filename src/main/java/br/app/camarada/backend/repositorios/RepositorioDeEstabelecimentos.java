package br.app.camarada.backend.repositorios;

import br.app.camarada.backend.entidades.Estabelecimento;
import br.app.camarada.backend.entidades.Produto;
import br.app.camarada.backend.entidades.Regiao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioDeEstabelecimentos extends JpaRepository<Estabelecimento, Long> {
    @Query(nativeQuery = true, value = "Select * from estabelecimento Order by avaliacao")
    List<Estabelecimento> findBestRatedEstablishment();

    List<Estabelecimento> findByRegiao(Regiao regiao);
}