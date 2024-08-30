package br.app.camarada.backend.repositorios;


import br.app.camarada.backend.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface RepositorioDeUsuario extends JpaRepository<Usuario, Long> {
     Usuario findByEstablishmentId(Long idDeEstabelecimento);

    Usuario findByEmail(String email);

    ArrayList<Usuario> findAll();

    Usuario findByIdDaSessaoWs(String sessaoId);
    @Query(nativeQuery = true, value = "Select * from usuario where entregador_id = :id")
    Usuario findByEntregadorId(@Param("id") Long idEntregador);
}
