package br.app.camarada.backend.repositorios;


import br.app.camarada.backend.entidades.Estatisticas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioEstatisticas extends JpaRepository<Estatisticas, Long> {


}
