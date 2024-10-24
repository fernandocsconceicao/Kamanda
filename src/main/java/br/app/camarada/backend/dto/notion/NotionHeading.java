package br.app.camarada.backend.dto.notion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.List;

@Data
@AllArgsConstructor
public class NotionHeading {
    private List<NotionConteudo> conteudos;
}
