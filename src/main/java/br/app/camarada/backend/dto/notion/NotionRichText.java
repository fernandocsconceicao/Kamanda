package br.app.camarada.backend.dto.notion;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotionRichText {
    private String type;
    private NotionText text;
}
