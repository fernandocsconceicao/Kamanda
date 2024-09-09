package br.app.camarada.backend.repositorios;

import br.app.camarada.backend.entidades.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioDePerfil extends JpaRepository<Perfil,Long> {

    void deleteByUsuarioId(Long usuarioId);
}
