package br.com.blogqateste.dto.autor;

import br.com.blogqateste.entity.Autor;

public record AutorRequestDTO(
        String id,
        String nome,
        String email,
        String bio,
        String fotoUrl
) {
    public static AutorRequestDTO fromEntity(Autor autor) {
        return new AutorRequestDTO(
                autor.getId(),
                autor.getNome(),
                autor.getEmail(),
                autor.getBio(),
                autor.getFotoUrl());
    }
}
