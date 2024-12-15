package br.app.camarada.backend.enums;

import lombok.Getter;

@Getter
public enum Categoria {
    BEBIDAS_ALCOLICAS("Bebidas Alcólicas 18+", "Bebidas alcólicas"),
    BEBIDAS_NAO_ALCOLICAS("Bebidas","Bebidas não-alcólicas"),
    PIZZA("Pizzaaaa","Pizza"),
    MASSAS("Massas","Massas"),
    VEGETARIANO("Comidas Vegetarianas","Vegetariano"),
    VEGETARIANOEVEGANO("Comidas Vegetarianas e Veganas", "Vegetariano e vegano"),
    VEGANO("Comidas Veganas", "Vegano"),
    LANCHES("Aquele lanchinho pra matar fome", "Lanche"),
    COMIDA_JAPONESA("Vai uma comida japonesa?","Comida japonesa"),
    COMIDA_ARABE("Que tal comida arabe?","Comida arabe"),
    COMIDA_BRASILEIRA("Nossa culinária brasileira", "Comida Brasileira");

    private String chamadaHome;
    private String nome;

    Categoria(String chamadaHome, String nome){

        this.nome= nome;
        this.chamadaHome= chamadaHome;
    }
}
