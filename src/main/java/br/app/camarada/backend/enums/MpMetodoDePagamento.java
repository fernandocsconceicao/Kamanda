package br.app.camarada.backend.enums;


public enum MpMetodoDePagamento {
    PIX("pix");
    private String valor;

    MpMetodoDePagamento(String v) {
        this.valor =v;
    }

    public String getValor() {
        return valor;
    }
}
