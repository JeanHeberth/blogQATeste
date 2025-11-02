package br.com.blogqateste.dto.post;

import br.com.blogqateste.entity.Categoria;
import br.com.blogqateste.entity.Post;
import br.com.blogqateste.entity.Tag;
import br.com.blogqateste.enums.TipoQa;

import java.time.LocalDateTime;
import java.util.List;

public record PostRequestDTO(

        String id,
        String titulo,
        String conteudo,
        TipoQa tipoQa,
        String autor,
        Categoria categoria,

        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao,

        List<Tag> tags,
        boolean publicado
) {
    public static Post toEntity(PostRequestDTO dto) {
        return new Post(
                dto.id(),
                dto.titulo(),
                dto.conteudo(),
                dto.tipoQa(),
                dto.autor(),
                dto.categoria(),
                dto.tags(),
                dto.dataCriacao(),
                dto.dataAtualizacao(),
                dto.publicado()
        );
    }
}
