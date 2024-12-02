package br.app.camarada.backend.client;

import br.app.camarada.backend.dto.RespostaHoraAtualWorldTime;
import br.app.camarada.backend.dto.notion.RequisicaoAppendNotionBlock;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "NotionClient", url = "https://api.notion.com")
public interface NotionClient {

    @RequestMapping(method = RequestMethod.GET, value = "v1/blocks/:id")
    RespostaHoraAtualWorldTime obterRegistros(@RequestParam("id") String id);

    @RequestMapping(method = RequestMethod.POST, value = "v1/pages")
    RespostaHoraAtualWorldTime tabularPublicacao(

            @RequestHeader(value = "Notion-Version") String notionVersion,
            @RequestHeader(value = "Authorization") String notionToken,
            @RequestBody RequisicaoAppendNotionBlock dto);
    @RequestMapping(method = RequestMethod.POST, value = "v1/pages")
    RespostaHoraAtualWorldTime tabularDenuncia(

            @RequestHeader(value = "Notion-Version") String notionVersion,
            @RequestHeader(value = "Authorization") String notionToken,
            @RequestBody RequisicaoAppendNotionBlock dto);
    @RequestMapping(method = RequestMethod.POST, value = "v1/pages")
    RespostaHoraAtualWorldTime tabularPublicacaoJson(

            @RequestHeader(value = "Notion-Version") String notionVersion,
            @RequestHeader(value = "Authorization") String notionToken,
            @RequestBody String dto);


}
