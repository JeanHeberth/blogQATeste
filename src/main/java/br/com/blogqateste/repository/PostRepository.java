package br.com.blogqateste.repository;

import br.com.blogqateste.entity.Post;
import br.com.blogqateste.enums.TipoQa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByTipoQa(TipoQa tipoQa);

    List<Post> findByPublicadoTrue();

    List<Post> findByAutorContainingIgnoreCase(String autor);

    Page<Post> findByTipoQaAndPublicadoTrue(TipoQa tipoQa, Pageable pageable);

    long countByPublicadoTrue();

    long countByTipoQa(TipoQa tipoQa);

    Page<Post> findByAutor(String autor, Pageable pageable);

    Page<Post> findByAutorAndPublicadoTrue(String autor, Pageable pageable);
}
