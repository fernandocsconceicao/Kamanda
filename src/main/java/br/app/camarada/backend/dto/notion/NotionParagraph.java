package br.app.camarada.backend.dto.notion;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class NotionParagraph {
    private List<NotionRichText> rich_text;
}
