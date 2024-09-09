package br.app.camarada.backend;

import br.app.camarada.backend.dto.RequisicaoRegistro;
import br.app.camarada.backend.entidades.Caixa;
import br.app.camarada.backend.enums.TipoConta;
import br.app.camarada.backend.repositorios.CaixaRepositorio;
import br.app.camarada.backend.servicos.ServicoParaUsuarios;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
//import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@AllArgsConstructor
public class BackendApplication  implements CommandLineRunner {
    private ServicoParaUsuarios servicoParaUsuarios;
    private CaixaRepositorio caixaRepositorio;
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Override
    public void run(String... args) {
        if (caixaRepositorio.findById(1L).isEmpty()) {
            caixaRepositorio.save(new Caixa());
        }
        if (!servicoParaUsuarios.existeAdminCadastrado()) {
            servicoParaUsuarios.registrarUsuario(new RequisicaoRegistro(
                    "admin@admin.com",
                    "123455678",
                    "1234567890",
                    "admin",
                    TipoConta.ADMIN));
        }
    }
}
