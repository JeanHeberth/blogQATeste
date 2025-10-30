package br.com.blogqateste.entity;


import br.com.blogqateste.enums.TipoQa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "posts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    private String id;

    @NotBlank(message = "O título não pode estar vazio")
    @Size(min = 5, max = 200, message = "O título deve ter entre 5 e 200 caracteres")
    private String titulo;

    @NotBlank(message = "O conteúdo não pode estar vazio")
    @Size(min = 20, message = "O conteúdo deve ter no mínimo 20 caracteres")
    private String conteudo;

    @NotNull(message = "O tipo de QA deve ser informado")
    private TipoQa tipoQa;

    @NotBlank(message = "O autor deve ser informado")
    private String autor;

    private LocalDateTime dataCriacao;

    private LocalDateTime dataAtualizacao;

    private boolean publicado;
}

