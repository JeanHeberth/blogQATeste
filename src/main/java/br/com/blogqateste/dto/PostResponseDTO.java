package br.com.blogqateste.dto;

import br.com.blogqateste.entity.Post;
import br.com.blogqateste.enums.TipoQa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public record PostResponseDTO(

        String id,

        String titulo,

        String conteudo,

        TipoQa tipoQa,

        String autor,

        LocalDateTime dataCriacao,

        LocalDateTime dataAtualizacao,

        boolean publicado
) {
    public static PostResponseDTO fromEntity(Post post) {
        return new PostResponseDTO(
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
