package br.app.camarada.backend.enums;


public enum FormaDePagamento {
    PIX("Pix QR Code"), CARTAO("Cartão de crédito ou débito"), SALDO(" Saldo da conta");
    final String name;
    FormaDePagamento(String name){
        this.name= name;
    }
}
