package br.com.blogqateste.entity;


import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tags")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    @Id
    private String id;

    @NotBlank(message = "O nome da tag não pode estar vazio")
    private String nome;
}

