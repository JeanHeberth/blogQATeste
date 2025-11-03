package br.com.blogqateste.repository.autor;

import br.com.blogqateste.entity.Autor;
import br.com.blogqateste.integration.config.AbstractIntegrationTest;
import br.com.blogqateste.repository.AutorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import(AbstractIntegrationTest.class)
class AutorRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private AutorRepository autorRepository;

    @Test
    void deveSalvarAutorComSucesso() {
        Autor autor = new Autor(
                "1",
                "Jean Heberth",
                "jean@gmail.com",
                "bioTestes",
                "fotoTeste");
        Autor salvo = autorRepository.save(autor);

        assertThat(salvo.getId()).isNotNull();
        assertThat(salvo.getNome()).isEqualTo("Jean Heberth");
    }

    @Test
    void deveBuscarAutorPorNome() {
        autorRepository.save(new Autor(
                "1",
                "Jessica Jasmine",
                "jean@gmail.com",
                "bioTestes",
                "fotoTeste"));
        boolean existe = autorRepository.existsByNome("Jessica Jasmine");

        assertThat(existe).isTrue();
    }
}

