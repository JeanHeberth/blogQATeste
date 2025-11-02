package br.com.blogqateste.entity;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "autores")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Autor {

    @Id
    private String id;

    @NotBlank(message = "O nome do autor é obrigatório")
    private String nome;

    @Email(message = "E-mail inválido")
    private String email;

    private String bio;
    private String fotoUrl;
}

