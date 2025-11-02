package br.com.blogqateste.dto.categoria;

import br.com.blogqateste.entity.Categoria;

public record CategoriaRequestDTO(
        String id,
        String nome,
        String descricao
) {
    public static CategoriaRequestDTO fromEntity(Categoria categoria) {
        return new CategoriaRequestDTO(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao());
    }
}
