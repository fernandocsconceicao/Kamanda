package br.app.camarada.backend.dto.notion;

import br.app.camarada.backend.enums.NotionBlockTypes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
public class RequisicaoAppendNotionBlock {
    private NotionParent parent;
    private NotionProperties properties;
    private List<NotionChildren> children;

    public static RequisicaoAppendNotionBlock construirPublicacaoEmDatabase(String database, String titulo, List<String> tags, String conteudo) throws JsonProcessingException {
        ArrayList<NotionTagName> notionTagNames = new ArrayList<>();
        tags.forEach(tag -> {
            notionTagNames.add(new NotionTagName(tag));
        });
        ArrayList<NotionRichText> notionRichTexts = new ArrayList<>();
        notionRichTexts.add(new NotionRichText("text", new NotionText(conteudo)));
        NotionParent notionParent = new NotionParent(database);

        ArrayList<NotionTitleObject> notionTitleObjects = new ArrayList<>();
        notionTitleObjects.add(new NotionTitleObject(new NotionText(titulo)));

        NotionProperties notionProperties = new NotionProperties(new NotionTitle(notionTitleObjects), new NotionTags(notionTagNames));
        ArrayList<NotionChildren> notionChildrenList = new ArrayList<>();

        NotionChildren notionChildren = new NotionChildren(
                "block",
                NotionBlockTypes.PARAGRAFO.obterValor(),
                new NotionParagraph(notionRichTexts)


        );
        notionChildrenList.add(notionChildren);
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        String formattedJson = writer.writeValueAsString(new RequisicaoAppendNotionBlock(notionParent, notionProperties, notionChildrenList));
        System.out.println("Resposta formatada: " + formattedJson);
        return new RequisicaoAppendNotionBlock(notionParent, notionProperties, notionChildrenList);
    }
}
