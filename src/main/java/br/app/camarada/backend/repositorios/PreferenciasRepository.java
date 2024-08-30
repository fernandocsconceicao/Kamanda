package br.app.camarada.backend.repositorios;

import br.app.camarada.backend.entidades.Preferencias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferenciasRepository extends JpaRepository<Preferencias, Long> {

}