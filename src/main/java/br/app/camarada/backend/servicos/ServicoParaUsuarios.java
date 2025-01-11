package br.app.camarada.backend.servicos;


//import br.app.camarada.backend.client.WorldTimeClient;

import br.app.camarada.backend.dto.*;
import br.app.camarada.backend.dto.google.RespostaLocalidadeGoogleMaps;
import br.app.camarada.backend.entidades.*;
import br.app.camarada.backend.enums.PlanoEstabelecimento;
import br.app.camarada.backend.enums.TipoConta;
import br.app.camarada.backend.exception.EmailJaCadastradoException;
import br.app.camarada.backend.repositorios.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ServicoParaUsuarios implements UserDetailsService {


    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    //    private final WorldTimeClient worldTimeClient;
    private final RepositorioDeUsuario repositorioDeUsuario;
    private final ServicoDePreferencias servicoDePreferencias;
    private final ServicoParaPerfil servicoParaPerfil;
    private final ServicoDeEmail servicoDeEmail;
    private final RepositorioDeContaFinanceira repositorioDeContaFinanceira;
    private final RepositorioDeEstabelecimentos repositorioDeEstabelecimentos;
    private final RepositorioDeEstabelecimentos_Endereco repositorioDeEstabelecimentosEndereco;
    private final RepositorioDeEnderecos repositorioDeEnderecos;
    private final ServicoDeEstabelecimentos servicoDeEstabelecimentos;
    private final ServicoDoGoogleMaps servicoDoGoogleMaps;
    private final RepositorioDePermissao repositorioDePermissao;
    private ObjectMapper objectMapper = new ObjectMapper();

    public AuthenticationResponseDto authenticate(RequisicaoDeAutenticacao dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(),
                        dto.getSenha()));
        byte[] imagemPerfil = null;


        Long IdEntregador = null;
        Usuario user = repositorioDeUsuario.findByEmail(dto.getEmail());
        if (user == null) {
            log.warn("User not Found");
            throw new UsernameNotFoundException("Usuário não encontrado : " + dto.getEmail());
        }
        String token = jwtService.gerarToken(user);
        if (user.getEstabelecimentoId() != null) {
            Estabelecimento estabelecimento = repositorioDeEstabelecimentos.findById(user.getEstabelecimentoId()).get();
            imagemPerfil = estabelecimento.getLogo();
        }

        return new AuthenticationResponseDto(
                token,
                user.getTipoConta().name(),
                user.getPrimeiroAcesso(),
                user.getId(),
                imagemPerfil,
                user.getEmailConfirmado(),
                IdEntregador,
                user.getEstabelecimentoId()

        );
    }

    public void editarEndereco(ReqEdicaoEndereco dto, DadosDeCabecalhos dadosDeCabecalhos) {
        Endereco endereco = repositorioDeEnderecos.findById(dadosDeCabecalhos.getIdEndereco()).get();

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

        Endereco save = repositorioDeEnderecos.save(endereco);
    }
//    @Transactional
    public AuthenticationResponseDto registrarUsuario(RequisicaoRegistro dto) {
        Usuario byEmail = repositorioDeUsuario.findByEmail(dto.getEmail());
        if (byEmail != null) {
            throw new EmailJaCadastradoException();
        }
        log.info("Saving user - email s%".format(dto.getEmail()));


        Long entregadorId = null;
        Long totemId = null;
        Long estabelecimentoId = null;
        Entregador entregador = null;
        PlanoEstabelecimento plano = null;


        String codigo = gerarCodigoDeConfirmacao();

        SimpleMailMessage mensagem = new SimpleMailMessage();

        mensagem.setFrom("fernando");
        mensagem.setTo(dto.getEmail());
        mensagem.setText("Seu código de confirmação de email da conta Kamanda é " + codigo);
        mensagem.setSubject("Confirmação de email");

        Preferencias preferencias = servicoDePreferencias.salvar(new Preferencias());
//        servicoDeEmail.enviarEmail(mensagem);
        if (dto.getTipoConta().equals(TipoConta.ESTABELECIMENTO)) {
            Estabelecimento estabelecimento = servicoDeEstabelecimentos.addEstablishment(dto.getPropriedadeDeEstabelecimentos());
            estabelecimentoId = estabelecimento.getId();
            plano = dto.getPropriedadeDeEstabelecimentos().getPlanoEstabelecimento();
        }
        List<Permissao> all = repositorioDePermissao.findAll();
        List<Permissao> permissoes = all.stream().filter(permissao1 -> permissao1.getNome().equals("USUARIO")).collect(Collectors.toList());

        Usuario user = new Usuario(
                null,
                encoder.encode(dto.getSenha()),
                dto.getTipoConta(),
                dto.getEmail(),
                dto.getNome(),
                permissoes,
                true,
                codigo,
                false,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "{}",
                null,
                null,
                null,
                null,
                null,
                estabelecimentoId,
                null,
                true,
                false,
                null,
                null,
                true

        );
        repositorioDeUsuario.save(user);
        user = repositorioDeUsuario.findByEmail(user.getEmail());
        Endereco endereco = Endereco.build(null);
        preferencias.setUsuarioId(user.getId());
        servicoDePreferencias.salvar(preferencias);
        user.setContaFinanceira(
                repositorioDeContaFinanceira.save(
                        new ContaFinanceira(
                                null,
                                user.getId(),
                                BigDecimal.ZERO,
                                null, LocalDateTime.now()))
        );

        endereco.setUsuario(user);
        repositorioDeEnderecos.save(endereco);
        Perfil perfil = servicoParaPerfil.criarPerfil(user);

        if (dto.getTipoConta().equals(TipoConta.ESTABELECIMENTO)) {
            Estabelecimento estabelecimento = repositorioDeEstabelecimentos.findById(estabelecimentoId).get();
            estabelecimento.setUsuarioId(user.getId());
        }

        System.out.println("1");

        ArrayList<Perfil> listaDePerfis = new ArrayList<>();
        listaDePerfis.add(perfil);
        user.setPerfil(listaDePerfis);
        user.setPerfilPrincipalId(perfil.getId());
        user.setPermissoes(new ArrayList<>());
        System.out.println("2");
        Usuario save = repositorioDeUsuario.save(user);
        String token = jwtService.gerarToken(user);
        System.out.println("3");

        return new AuthenticationResponseDto(
                token,
                TipoConta.USUARIO.name(),
                true,
                user.getId(),
                null,
                user.getEmailConfirmado(),
                null,
                estabelecimentoId
        );
    }

    private String gerarCodigoDeConfirmacao() {
        String CARACTERES_PERMITIDOS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        Integer tamanho = 6;

        StringBuilder codigo = new StringBuilder();
        for (int i = 0; i < tamanho; i++) {
            int indice = random.nextInt(CARACTERES_PERMITIDOS.length());
            char caracter = CARACTERES_PERMITIDOS.charAt(indice);
            codigo.append(caracter);
        }
        return codigo.toString();
    }

    public Usuario obterUsuario(String email) {
        return repositorioDeUsuario.findByEmail(email);
    }

    @Override
    public Usuario loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario user = obterUsuario(username);
        if (user == null) {
            log.warn("User not Found");
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
        return user;
    }


    public Boolean existeAdminCadastrado() {
        ArrayList<Usuario> all = repositorioDeUsuario.findAll();
        for (Usuario user : all) {
            if (user.getTipoConta().equals(TipoConta.ADMIN)) {
                return true;
            }
        }
        return false;
    }


    public Usuario obterPorId(Long id) {
        return repositorioDeUsuario.findById(id).get();
    }


    public void primeiroAcesso(Long usuarioId) {
        Usuario usuario = obterPorId(usuarioId);
        usuario.setPrimeiroAcesso(false);
        repositorioDeUsuario.save(usuario);
    }

    public void gerarCodigo(String email) {
        try {
            if (email != null && !email.isEmpty()) {
                Usuario usuario = repositorioDeUsuario.findByEmail(email);
                if (usuario != null) {

                    SimpleMailMessage mensagem = new SimpleMailMessage();
                    String codigo = gerarCodigoDeConfirmacao();
                    usuario.setCodigoEsqueciaSenha(codigo);
                    repositorioDeUsuario.save(usuario);
                    mensagem.setTo(email);
                    mensagem.setFrom("naoresponda@ubuntu.app.br");
                    mensagem.setText("Email para redefinição de senha de conta Ubuntu. " + codigo);
                    mensagem.setSubject("Redefinição de senha");

                    servicoDeEmail.enviarEmail(mensagem);
                }
            } else {
                throw new Exception("Endereço de email nulo ou vazio");
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public Boolean confirmarCodigoMudancaDeSenha(String codigo, String email) {
        Usuario usuario = repositorioDeUsuario.findByEmail(email);

        if (usuario.getCodigoEsqueciaSenha().equals(codigo)) {
            usuario.setCodigoEsqueciaSenha(null);
            usuario.setProntoParaMudarDeSenha(true);
            repositorioDeUsuario.save(usuario);
            return true;
        }

        return false;
    }

    public boolean confirmarEmail(String codigo, Long id) {
        Optional<Usuario> userOpt = repositorioDeUsuario.findById(id);
        if (userOpt.isPresent()) {
            Usuario usuario = userOpt.get();
            if (usuario.getCodigoConfirmacao().equalsIgnoreCase(codigo)) {
                usuario.setEmailConfirmado(true);
                repositorioDeUsuario.save(usuario);
                return true;
            }
        }
        return false;
    }


    public Boolean redefinirSenha(String email, String senha) {
        Usuario usuario = repositorioDeUsuario.findByEmail(email);
        log.info(usuario.getEmail());
        if (usuario.getProntoParaMudarDeSenha()) {
            log.info("texte");
            usuario.setSenhaAntiga(usuario.getSenha());
            usuario.setSenha(encoder.encode(senha));
            usuario.setProntoParaMudarDeSenha(false);
            repositorioDeUsuario.save(usuario);
            return true;
        }

        return false;
    }

    public void reenviarcodigoemail(String email) {
        Usuario usuario = repositorioDeUsuario.findByEmail(email);
        SimpleMailMessage mensagem = new SimpleMailMessage();

        mensagem.setFrom("naoresponda@ubuntu.app.br");
        mensagem.setTo(email);
        mensagem.setText("Seu código de confirmação de email da conta Ubuntu é " + usuario.getCodigoConfirmacao());
        mensagem.setSubject("Confirmação de email");
        servicoDeEmail.enviarEmail(mensagem);
    }

    public void salvarUsuario(Usuario usuario) {
        repositorioDeUsuario.save(usuario);
    }

    public void excluirConta(Long idUsuario) {
        Usuario usuario = repositorioDeUsuario.findById(idUsuario).get();
        servicoParaPerfil.excluirPerfil(usuario.getId());

        repositorioDeUsuario.deleteById(idUsuario);
    }

    public Boolean existeUsuarioCadastrado(String email) {
        Usuario byEmail = repositorioDeUsuario.findByEmail(email);
        return byEmail != null;
    }

    public EnderecoDto obterEndereco(DadosDeCabecalhos dadosDeCabecalhos) {
        Usuario usuario = repositorioDeUsuario.findById(dadosDeCabecalhos.getIdUsuario()).get();
        Endereco endereco = repositorioDeEnderecos.findById(usuario.getEnderecoId()).get();
        if (endereco == null) {
            endereco = repositorioDeEnderecos.save(Endereco.build(usuario));
            usuario.setEnderecoId(endereco.getId());
            repositorioDeUsuario.save(usuario);
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
}