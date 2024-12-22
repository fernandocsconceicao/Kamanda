package br.app.camarada.backend.client;

import br.app.camarada.backend.dto.RespostaHoraAtualWorldTime;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "WorldTimeClient",url = "https://timeapi.io")

public interface WorldTimeClient {
    @RequestMapping(method = RequestMethod.GET, value = "/api/time/current/zone?timeZone=America/Sao_Paulo")
    RespostaHoraAtualWorldTime buscarHora();


}
