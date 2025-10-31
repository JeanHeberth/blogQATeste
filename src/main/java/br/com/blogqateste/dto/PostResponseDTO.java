package br.com.blogqateste.dto;

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
){
}
