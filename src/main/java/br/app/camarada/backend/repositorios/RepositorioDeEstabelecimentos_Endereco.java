package br.app.camarada.backend.repositorios;

import br.app.camarada.backend.entidades.Estabelecimento;
import br.app.camarada.backend.entidades.Estabelecimento_Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioDeEstabelecimentos_Endereco extends JpaRepository<Estabelecimento_Endereco, Long> {

}