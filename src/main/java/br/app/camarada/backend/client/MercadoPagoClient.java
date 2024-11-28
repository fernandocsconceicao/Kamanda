package br.app.camarada.backend.client;


import br.app.camarada.backend.dto.mercadopago.MercadoPagoPixRequest;
import br.app.camarada.backend.dto.mercadopago.MercadoPagoPixResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "MercadoPagoClient",url = "https://api.mercadopago.com")
public interface MercadoPagoClient {
    @RequestMapping(method = RequestMethod.POST, value = "/v1/payments")
    MercadoPagoPixResponse criarPagamento(@RequestBody() MercadoPagoPixRequest body,
                                          @RequestHeader(name = "Authorization")
                                          String token);

    @RequestMapping(method = RequestMethod.GET, value = "/v1/payments/{id}")
    MercadoPagoPixResponse verificarPagamento(@PathVariable("id") String id,
                                              @RequestHeader(name = "Authorization")
                                              String token);
}
