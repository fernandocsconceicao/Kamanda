package br.app.camarada.backend.enums;

import lombok.Getter;

@Getter
public enum PlanoEstabelecimento {
    VITRINE(0.08, "Vitrine"), VITRINE_ENTREGA(0.08, "Vitrine + Entrega"), ENTREGA(0.15 , "Entrega");
    private Double tax;
    private String nome;
    PlanoEstabelecimento(Double taxa, String nome){
        this.tax = taxa;
        this.nome = nome;
    }
}
