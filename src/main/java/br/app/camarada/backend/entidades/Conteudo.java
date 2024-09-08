package br.app.camarada.backend.entidades;

import br.app.camarada.backend.enums.TipoPostagem;

public interface Conteudo {
   String obtertextoDaPostagem();
   TipoPostagem obterTipoDeConteudo();
   Perfil obterAutorPrincipal();

}
