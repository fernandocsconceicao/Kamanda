package br.app.camarada.backend.repositorios;


import br.app.camarada.backend.entidades.Caixa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaixaRepositorio extends JpaRepository<Caixa, Long> {


}
