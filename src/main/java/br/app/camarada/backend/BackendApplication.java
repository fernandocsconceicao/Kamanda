package br.app.camarada.backend;

import br.app.camarada.backend.dto.RequisicaoRegistro;
import br.app.camarada.backend.entidades.Caixa;
import br.app.camarada.backend.entidades.Permissao;
import br.app.camarada.backend.entidades.Regiao;
import br.app.camarada.backend.enums.TipoConta;
import br.app.camarada.backend.repositorios.CaixaRepositorio;
import br.app.camarada.backend.repositorios.RepositorioDePermissao;
import br.app.camarada.backend.repositorios.RepositorioDeRegiao;
import br.app.camarada.backend.servicos.ServicoParaUsuarios;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableFeignClients
@AllArgsConstructor
public class BackendApplication  implements CommandLineRunner {
    private ServicoParaUsuarios servicoParaUsuarios;
    private CaixaRepositorio caixaRepositorio;
    private RepositorioDePermissao repositorioDePermissao;
    private RepositorioDeRegiao regiaoRepository;

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Override
    public void run(String... args) {
        List<Regiao> regioesCadastradas = regiaoRepository.findAll();

        if (regioesCadastradas.isEmpty()) {
            List<String> nomesDeRegioes = new ArrayList<>();
            nomesDeRegioes.add("Poá / Ferraz / Calmon");
            nomesDeRegioes.add("Guaianases");
            nomesDeRegioes.add("Itaquera");
            nomesDeRegioes.add("Tatuapé");

            nomesDeRegioes.forEach(
                    nomeRegiao -> {
                        regiaoRepository.save(new Regiao(null, null, nomeRegiao, null));
                    }
            );
        }

        if(repositorioDePermissao.findAll().isEmpty()){
            ArrayList<Permissao> objects = new ArrayList<>();
            repositorioDePermissao.save(new Permissao(null, "USUARIO"));
            repositorioDePermissao.save(new Permissao(null, "ADMIN"));
        }

        if (caixaRepositorio.findById(1L).isEmpty()) {
            caixaRepositorio.save(new Caixa());
        }
        if (!servicoParaUsuarios.existeAdminCadastrado()) {
            servicoParaUsuarios.registrarUsuario(new RequisicaoRegistro(
                    "admin@admin.com",
                    "123455678",
                    "1234567890",
                    "admin",
                    TipoConta.ADMIN,
                    null));
        }
    }
}
