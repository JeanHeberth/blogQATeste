package br.com.blogqateste.integration;

import br.com.blogqateste.config.AbstractIntegrationTest;
import br.com.blogqateste.entity.Tag;
import br.com.blogqateste.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import(AbstractIntegrationTest.class)
class TagRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private TagRepository repository;

    @Test
    void deveSalvarTagComSucesso() {
        Tag tag = new Tag(null, "Spring Boot");
        Tag salva = repository.save(tag);

        assertThat(salva.getId()).isNotNull();
        assertThat(repository.existsByNome("Spring Boot")).isTrue();
    }

    @Test
    void deveBuscarTagPorNome() {
        repository.save(new Tag(null, "Testcontainers"));
        boolean existe = repository.existsByNome("Testcontainers");

        assertThat(existe).isTrue();
    }
}

