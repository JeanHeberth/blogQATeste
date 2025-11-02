package br.com.blogqateste.repository;

import br.com.blogqateste.entity.Autor;
import br.com.blogqateste.entity.Tag;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TagRepository extends MongoRepository<Tag, String> {
    boolean existsByNome(String nome);
}
