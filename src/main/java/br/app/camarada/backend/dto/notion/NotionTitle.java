package br.app.camarada.backend.dto.notion;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class NotionTitle {
    private List<NotionTitleObject> title;
}
