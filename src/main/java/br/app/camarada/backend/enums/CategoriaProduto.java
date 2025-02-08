package br.app.camarada.backend.enums;

import lombok.Getter;

@Getter
public enum CategoriaProduto {
    ELETRONICOS("Os melhores eletrônicos", "Eletrônicos", 0),
    ELETRODOMESTICOS("Eletrodomésticos para sua casa", "Eletrodomésticos", 1),
    CELULARES("Últimos modelos de celulares", "Celulares", 2),
    INFORMATICA("Tudo para informática", "Informática", 3),
    MOVEIS("Móveis para sua casa", "Móveis", 4),
    FERRAMENTAS("Ferramentas e utilidades", "Ferramentas", 5),
    ESPORTES("Produtos esportivos", "Esportes", 6),
    MODA("Moda para todas as ocasiões", "Moda", 7),
    LIVROS("Livros e literatura", "Livros", 8),
    BRINQUEDOS("Brinquedos e jogos", "Brinquedos", 9),
    SAUDE_E_BELEZA("Produtos de saúde e beleza", "Saúde e Beleza", 10),
    ALIMENTOS("Alimentos e bebidas", "Alimentos", 11),
    AUTOMOTIVO("Tudo para seu carro", "Automotivo", 12),
    PETS("Produtos para seus pets", "PETS", 13),
    DECORACAO("Decoração para sua casa", "Decoração", 14),
    JARDINAGEM("Produtos para jardinagem", "Jardinagem", 15),
    ARTIGOS_FESTA("Artigos para festas e eventos", "Artigos para Festa", 16),
    MUSICA("Instrumentos e acessórios musicais", "Música", 17),
    GAMES("Consoles e jogos", "Games", 18),
    OUTRO("Outros Produtos", "Outros", 19);

    private final String chamadaHome;
    private final String nome;
    private final Integer enumeracao;

    CategoriaProduto(String chamadaHome, String nome, Integer enumeracao) {
        this.chamadaHome = chamadaHome;
        this.nome = nome;
        this.enumeracao = enumeracao;
    }
}

