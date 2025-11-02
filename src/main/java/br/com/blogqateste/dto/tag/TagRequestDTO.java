package br.com.blogqateste.dto.tag;

import br.com.blogqateste.entity.Tag;

public record TagRequestDTO(
        String id,
        String nome
) {
    public static Tag toEntity(TagRequestDTO tag) {
        return new Tag(
                tag.id(),
                tag.nome()
        );
    }
}
