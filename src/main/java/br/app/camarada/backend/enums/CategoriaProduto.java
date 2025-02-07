package br.app.camarada.backend.enums;

import lombok.Getter;

@Getter
public enum CategoriaProduto {
    ELETRONICOS("Os melhores eletrônicos", "Eletrônicos"),
    ELETRODOMESTICOS("Eletrodomésticos para sua casa", "Eletrodomésticos"),
    CELULARES("Últimos modelos de celulares", "Celulares"),
    INFORMATICA("Tudo para informática", "Informática"),
    MOVEIS("Móveis para sua casa", "Móveis"),
    FERRAMENTAS("Ferramentas e utilidades", "Ferramentas"),
    ESPORTES("Produtos esportivos", "Esportes"),
    MODA("Moda para todas as ocasiões", "Moda"),
    LIVROS("Livros e literatura", "Livros"),
    BRINQUEDOS("Brinquedos e jogos", "Brinquedos"),
    SAUDE_E_BELEZA("Produtos de saúde e beleza", "Saúde e Beleza"),
    ALIMENTOS("Alimentos e bebidas", "Alimentos"),
    AUTOMOTIVO("Tudo para seu carro", "Automotivo"),
    PETS("Produtos para seus pets", "PETS"),
    DECORACAO("Decoração para sua casa", "Decoração"),
    JARDINAGEM("Produtos para jardinagem", "Jardinagem"),
    ARTIGOS_FESTA("Artigos para festas e eventos", "Artigos para Festa"),
    MUSICA("Instrumentos e acessórios musicais", "Música"),
    GAMES("Consoles e jogos", "Games"),
    OUTRO("Outros Produtos", "Outros");

    private final String chamadaHome;
    private final String nome;

    CategoriaProduto(String chamadaHome, String nome) {
        this.chamadaHome = chamadaHome;
        this.nome = nome;
    }
}
