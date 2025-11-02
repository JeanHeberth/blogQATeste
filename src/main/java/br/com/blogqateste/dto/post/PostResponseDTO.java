package br.com.blogqateste.dto.post;

import br.com.blogqateste.dto.categoria.CategoriaResponseDTO;
import br.com.blogqateste.dto.tag.TagResponseDTO;
import br.com.blogqateste.entity.Post;
import br.com.blogqateste.enums.TipoQa;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponseDTO(

        String id,
        String titulo,
        String conteudo,
        TipoQa tipoQa,
        String autor,
        CategoriaResponseDTO categoria,
        List<TagResponseDTO> tags,
        boolean publicado,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao

) {
    public static PostResponseDTO fromEntity(Post post) {
        return new PostResponseDTO(
                post.getId(),
                post.getTitulo(),
                post.getConteudo(),
                post.getTipoQa(),
                post.getAutor(),
                CategoriaResponseDTO.fromEntity(post.getCategoria()),
                post.getTags().stream().map(TagResponseDTO::fromEntity).toList(),
                post.isPublicado(),
                post.getDataCriacao(),
                post.getDataAtualizacao()
        );
    }
}
