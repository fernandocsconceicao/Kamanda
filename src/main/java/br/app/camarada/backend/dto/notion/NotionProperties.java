package br.app.camarada.backend.dto.notion;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotionProperties {
    private NotionTitle title;
    @JsonProperty("Tags")
    private NotionTags tags;
}
