package br.app.camarada.backend.repositorios;

import br.app.camarada.backend.entidades.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioDePerfilPessoal extends JpaRepository<Perfil,Long> {

}