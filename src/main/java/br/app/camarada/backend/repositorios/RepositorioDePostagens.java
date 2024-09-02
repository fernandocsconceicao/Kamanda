package br.app.camarada.backend.repositorios;

import br.app.camarada.backend.entidades.Publicacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioDePostagens extends JpaRepository<Publicacao,Long> {

}
