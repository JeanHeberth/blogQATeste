package br.com.blogqateste.dto.categoria;

import br.com.blogqateste.entity.Categoria;

public record CategoriaRequestDTO(
        String id,
        String nome,
        String descricao
) {
    public static Categoria toEntity(CategoriaRequestDTO categoria) {
        return new Categoria(
                categoria.id(),
                categoria.nome(),
                categoria.descricao()
        );
    }
}