package br.app.camarada.backend.enums;


public enum NotionBlockTypes {

    PARAGRAFO("paragraph"),
    TITULO_1("heading_1"),
    TITULO_2("heading_2"),
    TITULO_3("heading_3"),
    BULLETED_LIST_ITEM("bulleted_list_item"),
    NUMBERED_LIST_ITEM("numbered_list_item"),
    TAREFA("to_do"),
    BOTAO_ALTERNADO("toggle"),
    CODIGO("code"),
    IMAGEM("image"),
    VIDEO("video"),
    ARQUIVO("file"),
    PDF("pdf"),
    BOOKMARK("bookmark"),
    CALLOUT("callout"),
    QUOTE("quote"),
    DIVIDER("divider"),
    TABLE("table"),
    TABLE_ROW("table_row"),
    EMBED("embed");

    private final String valor;

    NotionBlockTypes(String valor) {
        this.valor = valor;
    }

    public String obterValor() {
        return this.valor;
    }
}
