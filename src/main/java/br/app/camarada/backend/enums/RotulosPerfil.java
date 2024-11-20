package br.app.camarada.backend.enums;

public enum RotulosPerfil {
    DESENVOLVEDOR("Desenvolvedor"),
    ENGENHEIRO("Engenheiro"),
    MEDICO("Médico"),
    ADVOGADO("Advogado"),
    PROFESSOR("Professor"),
    ANALISTA("Analista"),
    ENFERMEIRO("Enfermeiro"),
    GERENTE("Gerente"),
    ARQUITETO("Arquiteto"),
    TECNICO("Técnico"),
    CIENTISTA("Cientista"),
    FARMACEUTICO("Farmacêutico"),
    ADMINISTRADOR("Administrador"),
    ECONOMISTA("Economista"),
    BIOMEDICO("Biomédico"),
    PSICOLOGO("Psicólogo"),
    CONSULTOR("Consultor"),
    VETERINARIO("Veterinário"),
    CONTADOR("Contador"),
    DESIGNER("Designer"),
    MARKETING("Marketing"),
    PUBLICITARIO("Publicitário"),
    JORNALISTA("Jornalista"),
    ESCRITOR("Escritor"),
    EDITOR("Editor"),
    ATOR("Ator"),
    MUSICISTA("Musicista"),
    CHEFE_COZINHA("Chefe de Cozinha"),
    POLICIAL("Policial"),
    BOMBEIRO("Bombeiro"),
    MOTORISTA("Motorista"),
    PILOTO("Piloto"),
    MARINHEIRO("Marinheiro"),
    OPERADOR_MAQUINAS("Operador de Máquinas"),
    MECANICO("Mecânico"),
    ELETRICISTA("Eletricista"),
    PEDREIRO("Pedreiro"),
    SOLDADOR("Soldador"),
    AGRICULTOR("Agricultor"),
    ZOOTECNISTA("Zootecnista"),
    TECNICO_INFORMATICA("Técnico de Informática"),
    AGENTE_SEGURANCA("Agente de Segurança"),
    FUNCIONARIO_PUBLICO("Funcionário Público"),
    BANQUEIRO("Banqueiro"),
    CORRETOR_IMOVEIS("Corretor de Imóveis"),
    CORRETOR_SEGUROS("Corretor de Seguros");
    private final String valor;
    RotulosPerfil(String valor) {
        this.valor = valor;
    }
    public String obterValor() {
        return this.valor;
    }
}
