package br.app.camarada.backend.dto.notion;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotionChildren {
    private String object;
    private String type;
    private NotionParagraph paragraph;
}
