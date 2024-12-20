package br.app.camarada.backend.enums;


public enum Cores {
    VERMELHO("#FF0000", "Vermelho"),
    AZUL("#0000FF", "Azul"),
    VERDE("#00FF00", "Verde"),
    AMARELO("#FFFF00", "Amarelo"),
    PRETO("#000000", "Preto"),
    BRANCO("#FFFFFF", "Branco"),
    LARANJA("#FFA500", "Laranja"),
    ROSA("#FFC0CB", "Rosa"),
    ROXO("#800080", "Roxo"),
    CINZA("#808080", "Cinza"),
    MARROM("#8B4513", "Marrom"),
    AZUL_CLARO("#ADD8E6", "Azul Claro"),
    VERDE_CLARO("#90EE90", "Verde Claro"),
    TURQUESA("#40E0D0", "Turquesa"),
    DOURADO("#FFD700", "Dourado"),
    PRATA("#C0C0C0", "Prata"),
    CIANO("#00FFFF", "Ciano"),
    MAGENTA("#FF00FF", "Magenta"),
    LIMA("#00FF00", "Lima"),
    VIOLETA("#EE82EE", "Violeta"),
    BEGE("#F5F5DC", "Bege"),
    SALMAO("#FA8072", "Salmão"),
    LAVANDA("#E6E6FA", "Lavanda"),
    CHOCOLATE("#D2691E", "Chocolate");

    private final String codigoHex;
    private final String cor;

    // Construtor para associar o código hexadecimal e o nome da cor
    Cores(String codigoHex, String cor) {
        this.codigoHex = codigoHex;
        this.cor = cor;
    }

    // Getter para o código hexadecimal
    public String getCodigoHex() {
        return codigoHex;
    }

    // Getter para o nome da cor
    public String getCor() {
        return cor;
    }
}