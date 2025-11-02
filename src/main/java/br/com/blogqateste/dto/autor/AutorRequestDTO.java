package br.com.blogqateste.dto.autor;

import br.com.blogqateste.entity.Autor;

public record AutorRequestDTO(
        String id,
        String nome,
        String email,
        String bio,
        String fotoUrl
) {
    public static Autor toEntity(AutorRequestDTO autor) {
        return new Autor(
                autor.id(),
                autor.nome(),
                autor.email(),
                autor.bio(),
                autor.fotoUrl());
    }
}
