package br.com.blogqateste.dto.post;

import br.com.blogqateste.entity.Post;
import br.com.blogqateste.enums.TipoQa;

import java.time.LocalDateTime;

public record PostRequestDTO(

        String id,

        String titulo,

        String conteudo,

        TipoQa tipoQa,

        String autor,

        LocalDateTime dataCriacao,

        LocalDateTime dataAtualizacao,

        boolean publicado
) {
    public static PostRequestDTO fromEntity(Post post) {
        return new PostRequestDTO(
                post.getId(),
                post.getTitulo(),
                post.getConteudo(),
                post.getTipoQa(),
                post.getAutor(),
                post.getDataCriacao(),
                post.getDataAtualizacao(),
                post.isPublicado()
        );
    }

}
