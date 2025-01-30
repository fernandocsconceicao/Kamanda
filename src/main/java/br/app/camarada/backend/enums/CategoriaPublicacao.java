package br.app.camarada.backend.enums;

public enum CategoriaPublicacao {
    POLITICA(0, "Política"),
    BELEZA(1, "Beleza"),
    ECONOMIA(2, "Economia"),
    MODA(3, "Moda"),
    EDUCACAO(4, "Educação"),
    TODAS(10000,"TODAS");
    private Integer enumeracao;
    private String valor;

    CategoriaPublicacao(Integer enumeracao, String valor) {
        this.enumeracao = enumeracao;
        this.valor = valor;
    }

    public Integer obterEnumeracao() {
        return this.enumeracao;
    }
    public String obterValor() {
        return this.valor;
    }
}
