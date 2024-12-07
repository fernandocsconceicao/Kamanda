package br.app.camarada.backend.entidades;

import br.app.camarada.backend.enums.TipoPublicacao;

public interface Conteudo {
   String obtertextoDaPostagem();
   TipoPublicacao obterTipoDeConteudo();
   Perfil obterAutorPrincipal();

}
