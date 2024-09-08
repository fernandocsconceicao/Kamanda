package br.app.camarada.backend.repositorios;

import br.app.camarada.backend.entidades.Publicacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioDePublicacoes extends JpaRepository<Publicacao,Long> {

//    @Query(nativeQuery = true, value = "Select * from publicacao")
//    List<Publicacao> buscarPublicacoes();
}
