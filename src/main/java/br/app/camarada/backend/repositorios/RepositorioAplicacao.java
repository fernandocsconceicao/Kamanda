package br.app.camarada.backend.repositorios;


import br.app.camarada.backend.entidades.Aplicacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioAplicacao extends JpaRepository<Aplicacao, Long> {


}
