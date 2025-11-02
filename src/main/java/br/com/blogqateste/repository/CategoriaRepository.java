package br.com.blogqateste.repository;

import br.com.blogqateste.entity.Categoria;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoriaRepository extends MongoRepository<Categoria, String> {
    boolean existsByNome(String nome);
}
