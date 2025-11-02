package br.com.blogqateste.dto.categoria;

import br.com.blogqateste.entity.Categoria;

public record CategoriaResponseDTO(
        String nome,
        String descricao
) {
    public static CategoriaResponseDTO fromEntity(Categoria categoria) {
        return new CategoriaResponseDTO(
                categoria.getNome(),
                categoria.getDescricao());
    }
}
