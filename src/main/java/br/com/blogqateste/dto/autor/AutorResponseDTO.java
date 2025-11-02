package br.com.blogqateste.dto.autor;

import br.com.blogqateste.entity.Autor;

public record AutorResponseDTO(
        String nome,
        String email,
        String bio,
        String fotoUrl
) {
    public static AutorResponseDTO fromEntity(Autor autor) {
        return new AutorResponseDTO(
                autor.getNome(),
                autor.getEmail(),
                autor.getBio(),
                autor.getFotoUrl());
    }
}
