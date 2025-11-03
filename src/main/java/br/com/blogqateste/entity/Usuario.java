package br.com.blogqateste.entity;


import br.com.blogqateste.enums.UsuarioRole;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Document(collection = "usuarios")
public class Usuario {

    @Id
    private String id;

    private String nome;

    @Indexed(unique = true)
    private String email;

    /**
     * Senha armazenada com hash (BCrypt).
     */
    private String senha;

    @Builder.Default
    private UsuarioRole role = UsuarioRole.AUTOR;
}

