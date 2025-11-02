package br.com.blogqateste.entity;


import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "categorias")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {

    @Id
    private String id;

    @NotBlank(message = "O nome da categoria não pode estar vazio")
    private String nome;

    private String descricao;
}

