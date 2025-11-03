package br.com.blogqateste.repository.categoria;

import br.com.blogqateste.entity.Categoria;
import br.com.blogqateste.integration.config.AbstractIntegrationTest;
import br.com.blogqateste.repository.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import(AbstractIntegrationTest.class)
class CategoriaRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private CategoriaRepository repository;

    @Test
    void deveSalvarCategoriaComSucesso() {
        Categoria categoria = new Categoria(null, "Automação", "Testes automatizados");
        Categoria salva = repository.save(categoria);

        assertThat(salva.getId()).isNotNull();
        assertThat(repository.existsByNome("Automação")).isTrue();
    }
}

