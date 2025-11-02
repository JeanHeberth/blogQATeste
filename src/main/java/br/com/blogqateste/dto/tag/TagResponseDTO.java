package br.com.blogqateste.dto.tag;

import br.com.blogqateste.entity.Tag;

public record TagResponseDTO(
        String nome
) {

    public static TagResponseDTO fromEntity(Tag tag) {
        return new TagResponseDTO(tag.getNome());
    }
}
