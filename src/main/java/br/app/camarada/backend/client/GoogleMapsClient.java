package br.app.camarada.backend.client;


import br.app.camarada.backend.dto.RespostaDistanciaGoogleMaps;
import br.app.camarada.backend.dto.google.RespostaLocalidadeGoogleMapsWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "GoogleMapsClient",
        url = "https://maps.googleapis.com")
public interface GoogleMapsClient {

    @RequestMapping(method = RequestMethod.GET,
            value = "/maps/api/distancematrix/json")
    RespostaDistanciaGoogleMaps buscarDistancia(
            @RequestParam("key") String key,
            @RequestParam("origins") String origin,
            @RequestParam("destinations") String destination
    );

    @RequestMapping(method = RequestMethod.GET,
            value = "/maps/api/geocode/json")
    RespostaLocalidadeGoogleMapsWrapper buscarEndereco(
            @RequestParam String address,
            @RequestParam String key
    );
}
