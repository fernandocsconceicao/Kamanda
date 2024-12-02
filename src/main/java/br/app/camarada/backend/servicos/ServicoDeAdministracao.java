package br.app.camarada.backend.servicos;

import br.app.camarada.backend.client.NotionClient;
import br.app.camarada.backend.dto.DadosDeCabecalhos;
import br.app.camarada.backend.dto.RequisicaoDenuncia;
import br.app.camarada.backend.dto.notion.RequisicaoAppendNotionBlock;
import br.app.camarada.backend.entidades.Denuncia;
import br.app.camarada.backend.filtros.CustomServletWrapper;
import br.app.camarada.backend.repositorios.RepositorioDenuncia;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
@AllArgsConstructor
public class ServicoDeAdministracao {

    private RepositorioDenuncia repositorioDenuncia;
    private NotionClient notionClient;

    public Object denuncia(RequisicaoDenuncia dto, DadosDeCabecalhos dadosUsuario) throws JsonProcessingException {
        ArrayList<String> tags = new ArrayList<>();
        tags.add(dadosUsuario.getEmail());
        repositorioDenuncia.save(new Denuncia(null, dto.getMotivo(),
                dto.getDescricao(), dto.getIdPublicacao(), dadosUsuario.getIdUsuario(), dto.getIdPerfil()));
        notionClient.tabularDenuncia("2022-06-28",
                "Bearer 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",
                RequisicaoAppendNotionBlock.contruirDenuncia(
                        "150dd3360f128021999dece0067415fc", dto.getMotivo(), tags, dto.getDescricao(), dto.getIdPublicacao(), dadosUsuario.getIdUsuario(), dadosUsuario.getEmail()));



        return  null;
    }
}
