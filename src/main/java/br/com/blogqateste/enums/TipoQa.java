package br.com.blogqateste.enums;

public enum TipoQa {
    MANUAL("Manual"),
    AUTOMATIZADO("Automatizado"),
    AMBOS("Manual e Automatizado");

    private final String descricao;

    TipoQa(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

