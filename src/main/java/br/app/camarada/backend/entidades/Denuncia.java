package br.app.camarada.backend.entidades;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Denuncia {
    @Id
    @GeneratedValue
    private Long id;
    private String motivo;
    private String descricao;
    private Long idPublicacao;

    private Long idUsuarioAutor;

    private Long idPerfilDenunciado;

    public Denuncia(Long id, String motivo, String descricao, Long idPublicacao, Long idUsuarioAutor, Long idPerfilDenunciado) {
        this.id = id;
        this.motivo = motivo;
        this.descricao = descricao;
        this.idPublicacao = idPublicacao;
        this.idUsuarioAutor = idUsuarioAutor;
        this.idPerfilDenunciado = idPerfilDenunciado;
    }
}
