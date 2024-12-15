package br.app.camarada.backend.servicos;


import br.app.camarada.backend.dto.EnderecoDto;
import br.app.camarada.backend.dto.ReqCriacaoEndereco;
import br.app.camarada.backend.dto.ReqEdicaoEndereco;
import br.app.camarada.backend.dto.google.RespostaLocalidadeGoogleMaps;
import br.app.camarada.backend.entidades.Endereco;
import br.app.camarada.backend.repositorios.RepositorioDeEndereco;
import br.app.camarada.backend.repositorios.RepositorioDeEstabelecimentos_Endereco;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static br.app.camarada.backend.enums.TipoDeLocalidade.CLIENTE;

@Service
@Slf4j
@AllArgsConstructor
public class ServicoDeEndereco {
    private RepositorioDeEndereco enderecoRepository;
    private ServicoDoGoogleMaps servicoDoGoogleMaps;
    private RepositorioDeEstabelecimentos_Endereco estabelecimentoEnderecoRepository;


    public Endereco obterPorCliente(Long idCliente) {
        return enderecoRepository.obterPorCliente(idCliente);
    }

    public Endereco criarEndereco(ReqCriacaoEndereco dto, Long idCliente) {
        Boolean favorito = false;
        if (dto.getFavorito() != null) {
            favorito = dto.getFavorito();
        }
        StringBuilder enderecoCompleto = new StringBuilder();
        if (dto.getNumero() != null) {
            enderecoCompleto.append(" ," + dto.getNumero());
        }

        if (dto.getComplemento() != null) {
            enderecoCompleto.append(" ," + dto.getComplemento());
        }
        if (dto.getCep() != null) {
            enderecoCompleto.append(" ," + dto.getCep());
        }

        String placeId = servicoDoGoogleMaps.obterLocalidade(enderecoCompleto.toString()).getPlaceId();
        Endereco endereco = new Endereco(
                null,
                dto.getEndereco(),
                dto.getNumero(),
                dto.getComplemento(),
                null,
                favorito,
                dto.getRotulo(),
                dto.getCep(),
                enderecoCompleto.toString(),
                placeId,
                CLIENTE,
                dto.getCidade(),
                dto.getEstado(),
                null,
                null
        );
        Endereco enderecoSalvo = enderecoRepository.save(endereco);
        return enderecoSalvo;
    }

    public EnderecoDto obterEndereco(Long idCliente,Long idEstabelecimento) {

        Endereco endereco = null;
        if(idCliente != null){
            endereco = enderecoRepository.obterPorCliente(idCliente);
        } else if (idEstabelecimento != null) {
            endereco = estabelecimentoEnderecoRepository.findById(idEstabelecimento).get().getEndereco();
        }

        return new EnderecoDto(
                endereco.getId(),
                endereco.getEndereco(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getEnderecoCompleto(),
                endereco.getCep(),
                endereco.getRotulo(),
                endereco.getCidade(),
                endereco.getEstado()
        );
    }
    public EnderecoDto obterEnderecoPorEstabelecimento(Long idEstabelecimento) {
        Endereco endereco = enderecoRepository.obterPorEstabelecimento(idEstabelecimento);
        return new EnderecoDto(
                endereco.getId(),
                endereco.getEndereco(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getEnderecoCompleto(),
                endereco.getCep(),
                endereco.getRotulo(),
                endereco.getCidade(),
                endereco.getEstado()
        );
    }


    public void editarEndereco(ReqEdicaoEndereco dto, Long idCliente, Long idEstabelecimento) {
        Endereco endereco = null;
        if(idCliente != null){
            endereco = enderecoRepository.obterPorCliente(idCliente);
        } else if (idEstabelecimento != null) {
            endereco = estabelecimentoEnderecoRepository.findById(idEstabelecimento).get().getEndereco();
        }
        String enderecoCompletoParaGM = dto.getEndereco() + ", " + dto.getCidade() + ", " + dto.getEstado();

        RespostaLocalidadeGoogleMaps respostaLocalidadeGoogleMaps = servicoDoGoogleMaps.obterLocalidade(enderecoCompletoParaGM);
        endereco.setGooglePlaceId(respostaLocalidadeGoogleMaps.getPlaceId());
        endereco.setNumero(dto.getNumero());
        endereco.setComplemento(dto.getComplemento());
        endereco.setCep(dto.getCep());
        endereco.setCidade(dto.getCidade());
        endereco.setEstado(dto.getEstado());
        endereco.setEnderecoCompleto(respostaLocalidadeGoogleMaps.getEnderecoCompleto());
        endereco.setEndereco(dto.getEndereco());
        endereco.setNumero(dto.getNumero());
        endereco.setComplemento(dto.getComplemento());
        endereco.setRotulo(dto.getRotulo());
        endereco.setFavorito(true);

        Endereco save = enderecoRepository.save(endereco);
    }

    public Endereco criarEndereco(Endereco endereco) {
        return enderecoRepository.save(endereco);
    }
}
