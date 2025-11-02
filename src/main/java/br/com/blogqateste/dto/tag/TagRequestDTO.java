package br.com.blogqateste.dto.tag;

import br.com.blogqateste.entity.Tag;

public record TagRequestDTO(
        String id,
        String nome
){
    public static TagRequestDTO fromEntity(Tag tag) {
        return new TagRequestDTO(
                tag.getId(),
                tag.getNome());
    }
}
