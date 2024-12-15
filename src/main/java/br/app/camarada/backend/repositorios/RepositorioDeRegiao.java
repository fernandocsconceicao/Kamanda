package br.app.camarada.backend.repositorios;

import br.app.camarada.backend.entidades.Regiao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioDeRegiao extends JpaRepository<Regiao, Long> {

}
