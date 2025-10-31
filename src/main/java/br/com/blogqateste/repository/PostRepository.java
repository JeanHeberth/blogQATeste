package br.com.blogqateste.repository;

import br.com.blogqateste.entity.Post;
import br.com.blogqateste.enums.TipoQa;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByTipoQa(TipoQa tipoQa);

    List<Post> findByPublicadoTrue();
}
