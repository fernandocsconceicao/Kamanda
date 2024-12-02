package br.app.camarada.backend.repositorios;


import br.app.camarada.backend.entidades.Denuncia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioDenuncia extends JpaRepository<Denuncia, Long> {


}
