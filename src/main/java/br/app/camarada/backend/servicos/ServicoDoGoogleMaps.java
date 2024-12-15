package br.app.camarada.backend.servicos;

import br.app.camarada.backend.client.GoogleMapsClient;
import br.app.camarada.backend.dto.DistanciaDto;
import br.app.camarada.backend.dto.RespostaDistanciaGoogleMaps;
import br.app.camarada.backend.dto.google.GoogleMapsRows;
import br.app.camarada.backend.dto.google.GoogleMapsRowsElement;
import br.app.camarada.backend.dto.google.RespostaLocalidadeGoogleMaps;
import br.app.camarada.backend.dto.google.RespostaLocalidadeGoogleMapsWrapper;
import br.app.camarada.backend.entidades.Endereco;
import br.app.camarada.backend.exception.ErroPadrao;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ServicoDoGoogleMaps {


    private GoogleMapsClient googleMapsClient;

    private final String key = "AIzaSyD_C6y0Iyz187calN490m-zk4HUIvtoG6Q";

    public RespostaLocalidadeGoogleMaps obterLocalidade(String enderecoCompleto) {
        RespostaLocalidadeGoogleMapsWrapper resposta = googleMapsClient.buscarEndereco(enderecoCompleto, key);
        if (!resposta.getResults().isEmpty()) {
            RespostaLocalidadeGoogleMaps respostaLocalidadeGoogleMaps = resposta.getResults().get(0);
            return respostaLocalidadeGoogleMaps;
        }
        return null;
    }

    public DistanciaDto calcularDistancia(Endereco enderecoCliente, Endereco enderecoEstabelecimento)  {
        System.out.println("place_id:" + enderecoCliente.getEnderecoCompleto());
        RespostaDistanciaGoogleMaps respostaDistanciaGoogleMaps = googleMapsClient.buscarDistancia(key,
                enderecoCliente.getEnderecoCompleto(),
                enderecoEstabelecimento.getEnderecoCompleto());
        try {
            GoogleMapsRows googleMapsRows = respostaDistanciaGoogleMaps.getRows().get(0);
            List<GoogleMapsRowsElement> collect = googleMapsRows.getElements().stream()
                    .filter(element -> element.getDistance().getDistancia() != null).collect(Collectors.toUnmodifiableList());
            Integer metros = collect.get(0).getDistance().getMetros();
            Double distancia = (metros / 1000.0);
            return new DistanciaDto(distancia, collect.get(0).getDistance().getDistancia());
        } catch (Exception e) {
            throw new ErroPadrao("Erro ao calcular distancia no Google Maps");
        }


    }

    public DistanciaDto calcularDistancia(Endereco enderecoInicial, String enderecoFinal) {
        System.out.println("place_id:" + enderecoInicial.getGooglePlaceId());
        RespostaDistanciaGoogleMaps respostaDistanciaGoogleMaps = googleMapsClient.buscarDistancia(key,
                enderecoInicial.getEnderecoCompleto(), enderecoFinal);

        GoogleMapsRows googleMapsRows = respostaDistanciaGoogleMaps.getRows().get(0);
        List<GoogleMapsRowsElement> collect = googleMapsRows.getElements().stream()
                .filter(element -> element.getDistance().getDistancia() != null).collect(Collectors.toUnmodifiableList());
        Integer metros = collect.get(0).getDistance().getMetros();
        Double distancia = (metros / 1000.0);

        return new DistanciaDto(distancia, collect.get(0).getDistance().getDistancia());

    }

}
