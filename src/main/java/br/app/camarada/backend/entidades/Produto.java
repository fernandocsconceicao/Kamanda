package br.app.camarada.backend.entidades;

import br.app.camarada.backend.enums.Categoria;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Produto {
    @Id
    @GeneratedValue
    private Long id;
    private String nome;
    @ManyToOne
    @NotNull
    private Estabelecimento estabelecimento;
    @Lob
    private byte[] imagem;
    private LocalDate dataDeCriacao;
    @Enumerated
    private Categoria categoria;
    private BigDecimal preco;
    private Double avaliacao;
    private LocalDateTime dataExibicao;
    private Long idRegiao;

}
