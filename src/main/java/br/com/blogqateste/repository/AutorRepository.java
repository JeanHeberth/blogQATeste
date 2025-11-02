package br.com.blogqateste.repository;

import br.com.blogqateste.entity.Autor;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AutorRepository extends MongoRepository<Autor, String> {
    boolean existsByNome(String nome);
}
