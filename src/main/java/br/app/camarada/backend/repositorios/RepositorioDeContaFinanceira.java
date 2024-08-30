package br.app.camarada.backend.repositorios;

import br.app.camarada.backend.entidades.ContaFinanceira;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioDeContaFinanceira extends JpaRepository<ContaFinanceira, Long> {

    @Query(nativeQuery = true, value = "Select * from conta_financeira where id_usuario = :id")
    ContaFinanceira obterPorUsuario(@Param("id") Long idUsuario);
}
