package br.app.camarada.backend.servicos;


import br.app.camarada.backend.dto.*;
import br.app.camarada.backend.dto.google.RespostaLocalidadeGoogleMaps;
import br.app.camarada.backend.entidades.*;
import br.app.camarada.backend.enums.PlanoEstabelecimento;
import br.app.camarada.backend.enums.TextosBundle;
import br.app.camarada.backend.enums.TipoDeLocalidade;
import br.app.camarada.backend.enums.TipoServico;
import br.app.camarada.backend.exception.ErroPadrao;
import br.app.camarada.backend.exception.ExcessaoDeEnderecoInvalido;
import br.app.camarada.backend.repositorios.*;
import br.app.camarada.backend.utilitarios.StringUtils;
import br.app.camarada.backend.utilitarios.UtilitarioBundle;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ServicoDeEstabelecimentos {
    private RepositorioDeProdutos productRepository;
    private RepositorioDeEstabelecimentos repositorioDeEstabelecimento;
    private ServicoDaRevolucaoBrasileira servicoDaRevolucaoBrasileira;
    private RepositorioDeUsuario repositorioDeUsuario;
    private RepositorioRegiao repositorioRegiao;
    private RepositorioDeEstabelecimentos_Endereco estabelecimentoEnderecoRepository;
    private ServicoDoGoogleMaps servicoDoGoogleMaps;
    private ServicoDeEndereco servicoDeEndereco;
    private RepositorioDeRegiao regiaoRepository;


    public TelaMeuEstabelecimento obterTelaEstabelecimento(Long idEstabelecimento) {
        Estabelecimento estabelecimento = obterEstabelecimento(idEstabelecimento);
        List<CartaoEstabelecimento> cartoes = new ArrayList<>();
        cartoes.add(
                new CartaoEstabelecimento(UtilitarioBundle.obterMensagem(TextosBundle.CARTAO_MEU_ESTABELECIMENTO_VALOR_A_RECEBER),
                        estabelecimento.getValorAReceber().toString())
        );
        cartoes.add(
                new CartaoEstabelecimento(UtilitarioBundle.obterMensagem(TextosBundle.CARTAO_MEU_ESTABELECIMENTO_PLANO_ATUAL),
                        estabelecimento.getPlano().getNome())
        );

        return new TelaMeuEstabelecimento(cartoes);
    }
    @Transactional
    public Estabelecimento addEstablishment(PropriedadesDoEstabelecimento dto) {
        try {

            Endereco endereco = servicoDeEndereco.criarEndereco(Endereco.build(null));
            return repositorioDeEstabelecimento.save(
                    Estabelecimento.build(dto, endereco)
            );

        } catch (IllegalArgumentException | OptimisticLockingFailureException e) {
            String message = new StringBuilder
                    ("Erro ao criar produto: ").append(e.getMessage())
                    .toString();
            log.error(message);
            throw new EstablishmentCreationException(message);
        }
    }

    public List<Estabelecimento> findBestRatedEstablishment() {
        return repositorioDeEstabelecimento.findBestRatedEstablishment();
    }

    public Estabelecimento obterEstabelecimento(Long id) {
        Optional<Estabelecimento> optionalEstablishment = repositorioDeEstabelecimento.findById(id);
        return optionalEstablishment.orElse(null);
    }

    public Estabelecimento_Endereco obterEstabelecimento_Endereco(Long id) {
        Optional<Estabelecimento_Endereco> optionalEstablishment = estabelecimentoEnderecoRepository.findById(id);
        return optionalEstablishment.orElse(null);
    }

    public void updateEstablishmentProperties(Produto produto) {

        List<Produto> establishmentProdutos = productRepository.findByEstablishmentId(produto.getEstabelecimentoId());
        boolean veganStamp = false;
        boolean vegetarianStamp = false;


        //TODO: Economizar chamadas ao banco
        Estabelecimento estabelecimento = repositorioDeEstabelecimento.findById(produto.getEstabelecimentoId()).get();
        estabelecimento.setVeganStamp(veganStamp);
        estabelecimento.setVegetarianStamp(vegetarianStamp);
        repositorioDeEstabelecimento.save(estabelecimento);
    }
    public ResPrimeiroAcessoEstabelecimento primeiroAcesso(ReqPrimeiroAcessoEstabelecimento dto) {
        Estabelecimento_Endereco estabelecimento = estabelecimentoEnderecoRepository.findById(dto.getIdDeEstabelecimento()).get();

        Regiao regiao = regiaoRepository.findById(dto.getIdRegiao()).get();

        estabelecimento.setLogo(dto.getLogo().getBytes());
        if (dto.getValorMinimoDePedido() != null)
            estabelecimento.setMinOrder(dto.getValorMinimoDePedido());
        if (dto.getInicioExpediente() != null)
            estabelecimento.setInicioExpediente(
                    LocalTime.now()
                            .withHour(dto.getInicioExpediente().getHours())
                            .withMinute(dto.getInicioExpediente().getMinutes())
            );

        if (dto.getFimExpediente() != null)
            estabelecimento.setFimExpediente(
                    LocalTime.now()
                            .withHour(dto.getFimExpediente().getHours())
                            .withMinute(dto.getFimExpediente().getMinutes())
            );
        Endereco endereco = estabelecimento.getEndereco();
        endereco.setNumero(dto.getNumero());
        endereco.setComplemento(dto.getComplemento());
        endereco.setCep(dto.getCep());
        endereco.setCidade(dto.getCidade());
        endereco.setEstado(dto.getEstado());

        StringBuilder enderecoCompleto = new StringBuilder();

        enderecoCompleto.append(dto.getRua());
        enderecoCompleto.append(", ");
        enderecoCompleto.append(dto.getNumero());
        if (dto.getComplemento() != null) {
            enderecoCompleto.append(", ");
            enderecoCompleto.append(dto.getComplemento());
        }
        enderecoCompleto.append(", ");
        enderecoCompleto.append(dto.getCidade());
        enderecoCompleto.append(", ");
        enderecoCompleto.append(dto.getEstado());

        estabelecimento.setEnderecoCompleto(enderecoCompleto.toString());
        endereco.setEnderecoCompleto(enderecoCompleto.toString());

        RespostaLocalidadeGoogleMaps respostaLocalidadeGoogleMaps;
        try {
            respostaLocalidadeGoogleMaps = servicoDoGoogleMaps.obterLocalidade(enderecoCompleto.toString());
            endereco.setGooglePlaceId(respostaLocalidadeGoogleMaps.getPlaceId());
        } catch (NullPointerException e) {
            throw new ExcessaoDeEnderecoInvalido();
        }
        estabelecimento.setRegiao(regiao);
        estabelecimentoEnderecoRepository.save(estabelecimento);

        Usuario usuario = repositorioDeUsuario.findByEstabelecimentoId(dto.getIdDeEstabelecimento());
        usuario.setPrimeiroAcesso(false);
        usuario.setRegiao(regiao);
        repositorioDeUsuario.save(usuario);
        return new ResPrimeiroAcessoEstabelecimento(estabelecimento.getLogo());
    }
    public void editarEstabelecimento(PostagemDeEdicaoDeEstabelecimento dto) {
        Optional<Estabelecimento_Endereco> byId = estabelecimentoEnderecoRepository.findById(dto.getIdDeEstabelecimento());
        Estabelecimento_Endereco estabelecimento = byId.get();
        if (byId.isPresent()) {
            if (dto.getImage() != null) {
                estabelecimento.setLogo(dto.getImage());
            }
            estabelecimento.setName(dto.getNome());

            Endereco endereco = estabelecimento.getEndereco();
            String enderecoCompleto = dto.getEndereco() + ", " + dto.getCidade() + ", " + dto.getEstado();

            if (endereco != null) {
                RespostaLocalidadeGoogleMaps respostaLocalidadeGoogleMaps;
                try {
                    respostaLocalidadeGoogleMaps = servicoDoGoogleMaps.obterLocalidade(enderecoCompleto);
                    endereco.setGooglePlaceId(respostaLocalidadeGoogleMaps.getPlaceId());
                } catch (NullPointerException e) {
                    throw new ExcessaoDeEnderecoInvalido();
                }
                endereco.setNumero(dto.getNumero());
                endereco.setComplemento(dto.getComplemento());
                endereco.setCep(dto.getCep());
                endereco.setCidade(dto.getCidade());
                endereco.setEstado(dto.getEstado());
                estabelecimento.setEnderecoCompleto(dto.getEndereco());
                estabelecimento.setEndereco(endereco);
                estabelecimento.setTelefone(dto.getTelefone());
            } else {
                RespostaLocalidadeGoogleMaps respostaLocalidadeGoogleMaps = servicoDoGoogleMaps.obterLocalidade(enderecoCompleto);

                Endereco endereco1 = new Endereco(
                        null,
                        dto.getEndereco(),
                        dto.getNumero(),
                        dto.getComplemento(),
                        null,
                        true,
                        "estabelecimento",
                        dto.getCep(),
                        respostaLocalidadeGoogleMaps.getEnderecoCompleto(),
                        respostaLocalidadeGoogleMaps.getPlaceId(),
                        TipoDeLocalidade.ESTABELECIMENTO,
                        dto.getCidade(),
                        dto.getEstado(),
                        estabelecimento,
                        null
                );
                Endereco e = servicoDeEndereco.criarEndereco(endereco1);
                estabelecimento.setEndereco(e);
            }

            estabelecimento.setCep(dto.getCep());
            estabelecimento.setDescription(dto.getDescricao());
            estabelecimento.setTelefone(dto.getTelefone());

            estabelecimento.setMinOrder(dto.getValorMinimo());

            estabelecimentoEnderecoRepository.save(estabelecimento);
        }
    }


    public List<ProdutoParaEstabelecimento> listarProdutosDeEstabelecimentos(Long id) {
        return ProdutoParaEstabelecimento.build(productRepository.findByEstablishmentId(id));
    }

    public Boolean alternardisponibilidade(Long idEstabelecimento) {
        Optional<Estabelecimento> optionalEstabelecimento = repositorioDeEstabelecimento.findById(idEstabelecimento);
        AtomicReference<Estabelecimento> estabelecimento = new AtomicReference<>(new Estabelecimento());
        optionalEstabelecimento.ifPresent(e -> {
            e.setOpenClosed(!e.getOpenClosed());
            estabelecimento.set(repositorioDeEstabelecimento.save(e));

        });
        return estabelecimento.get().getOpenClosed();

    }

    public void salvar(Estabelecimento_Endereco estabelecimento) {
        estabelecimentoEnderecoRepository.save(estabelecimento);
    }
    public MeuPerfilScreen obterTelaMinhaLoja(Long idEstabelecimento) {
        Estabelecimento_Endereco estabelecimento = obterEstabelecimento_Endereco(idEstabelecimento);

        if (estabelecimento == null)
            throw new ErroPadrao("Id de estabelecimento inválido. " + idEstabelecimento);
        return MeuPerfilScreen.build(estabelecimento);
    }
    public Boolean alterarHorarioFuncionamento(ReqAlterarHorarioFuncionamento dto, Long id) {
        Optional<Estabelecimento> estabelecimentoOpt = repositorioDeEstabelecimento.findById(id);
        if (estabelecimentoOpt.isPresent()) {
            Estabelecimento estabelecimento = estabelecimentoOpt.get();
            estabelecimento.setInicioExpediente(LocalTime.now().withHour(dto.getInicioExpediente().getHours()).withMinute(dto.getInicioExpediente().getMinutes()));
            estabelecimento.setFimExpediente(LocalTime.now().withHour(dto.getFimExpediente().getHours()).withMinute(dto.getFimExpediente().getMinutes()));
            repositorioDeEstabelecimento.save(estabelecimento);
            log.info("Horario de funcionamento alterado. Estabelecimento id " + id + ".");
            return true;
        } else {
            log.error("Falha ao alterar horario de funcionamento: Estabelecimento inexistente." + "Estabelecimento id " + id);
            return false;
        }
    }

    public List<Estabelecimento> buscarEstabelecimentosAbertosPorRegiao(Regiao regiao) {
        List<Estabelecimento> byRegiao = repositorioDeEstabelecimento.findByRegiao(regiao);
        ZonedDateTime horaAtual = ZonedDateTime.now();
        System.out.println(byRegiao.size() + "estabelecimentos ");
        List<Estabelecimento> estabelecimentosAbertos = byRegiao.stream().filter(estabelecimento -> {

            if (estabelecimento.getAprovado()
            ) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Hora abertura: ");
                stringBuilder.append(estabelecimento.getInicioExpediente().toString());
                stringBuilder.append("Hora atual: ");
                stringBuilder.append(horaAtual.toLocalTime().toString());
                stringBuilder.append("Hora fechamento: ");
                stringBuilder.append(estabelecimento.getFimExpediente().toString());

                System.out.println(stringBuilder);

                if (estabelecimento.getInicioExpediente().isBefore(

                        horaAtual.toLocalTime().minusHours(3)) &&
                        estabelecimento.getFimExpediente().isAfter(horaAtual.toLocalDate().atStartOfDay().toLocalTime())) {

                    return true;
                }
                return false;

            } else return false;
        }).collect(Collectors.toUnmodifiableList());
        System.out.println(estabelecimentosAbertos.size() + " estabelecimentos abertos");

        return estabelecimentosAbertos;
    }

    public void solicitarEntrega(SolicitacaoDeEntrega dto, Long idEstabelecimento, Long usuarioId, Usuario cliente, Pedido pedido) {
        Optional<Estabelecimento_Endereco> byId = estabelecimentoEnderecoRepository.findById(idEstabelecimento);
        if (byId.isPresent()) {
            Estabelecimento_Endereco estabelecimento = byId.get();
            if (
                    estabelecimento.getPlano() == PlanoEstabelecimento.VITRINE_ENTREGA ||
                            estabelecimento.getPlano() == PlanoEstabelecimento.ENTREGA
            ) {
                if (dto.getTipoServico() == TipoServico.COMPRA) {
                    servicoDaRevolucaoBrasileira.adicionarPedidoAFila(
                            pedido.getId(),
                            usuarioId,
                            idEstabelecimento,
                            true,
                            dto,
                            new DadosFinanceirosEntrega(cliente.getSimulacaoFrete(), null, cliente.getSimulacaoPrecoFinalDaSimulacao(), null)
                    );

                } else if (dto.getTipoServico() == TipoServico.ENTREGA) {
                    servicoDaRevolucaoBrasileira.adicionarPedidoAFila(
                            null,
                            usuarioId,
                            idEstabelecimento,
                            true,
                            dto,
                            new DadosFinanceirosEntrega(estabelecimento.getValorTotalSimulacaoEntrega(), null, estabelecimento.getValorTotalSimulacaoEntrega(), null)
                    );
                }

            } else {
                log.warn(" Pedido realizado para estabelecimento(" + estabelecimento.getName() + ") sem entrega");
            }
        }
    }


    public void excluirEstabelecimento(Long idEstabelecimento) {
        Estabelecimento_Endereco estabelecimento = estabelecimentoEnderecoRepository.findById(idEstabelecimento).get();
        estabelecimento.setName("");
        estabelecimento.setCep("");
        estabelecimento.setDescription("");
        estabelecimento.setEnderecoCompleto("");
        estabelecimento.setTelefone("");
        Endereco endereco = estabelecimento.getEndereco();
        endereco.setEndereco("");
        endereco.setEnderecoCompleto("");
        endereco.setGooglePlaceId("");
        endereco.setComplemento("");
        endereco.setEstado("");
        endereco.setCidade("");
        endereco.setCep("");
        estabelecimento.setEndereco(endereco);
        estabelecimentoEnderecoRepository.save(estabelecimento);
    }

    public ResConfirmacaoSolicitacaoDeEntrega obterTelaDeConfirmacaoDeEntrega(
            Long idEstabelecimento,
            ReqConfirmacaoSolicitacaoDeEntrega req
    ) {
        Estabelecimento_Endereco estabelecimentoEndereco = estabelecimentoEnderecoRepository.findById(idEstabelecimento).get();
        DistanciaDto distancia = servicoDoGoogleMaps.calcularDistancia(estabelecimentoEndereco.getEndereco(), req.getEnderecoFinal());


        BigDecimal valorCorrida = BigDecimal.valueOf(distancia.getDistanciaEmKm())
                .multiply(BigDecimal.valueOf(2));
        BigDecimal comissaoUbuntu = valorCorrida.multiply(BigDecimal.valueOf(1.15)).subtract(valorCorrida);

        estabelecimentoEndereco.setValorTotalSimulacaoEntrega(valorCorrida);
        estabelecimentoEndereco.setValorUbuntuSimulacaoEntrega(comissaoUbuntu);
        estabelecimentoEnderecoRepository.save(estabelecimentoEndereco);
        return new ResConfirmacaoSolicitacaoDeEntrega(
                "" + distancia.getDistanciaGoogle(),
                StringUtils.formatPrice(valorCorrida),
                req.getEnderecoFinal(),
                estabelecimentoEndereco.getEndereco().getEnderecoCompleto(),
                valorCorrida
        );
    }
}
