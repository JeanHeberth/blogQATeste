package br.com.blogqateste.repository.post;

import br.com.blogqateste.entity.Post;
import br.com.blogqateste.enums.TipoQa;
import br.com.blogqateste.integration.config.AbstractIntegrationTest;
import br.com.blogqateste.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import(AbstractIntegrationTest.class)
class PostRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve salvar um post com sucesso")
    void deveSalvarPostComSucesso() {
        Post post = Post.builder()
                .titulo("Introdução ao Selenium WebDriver")
                .conteudo("Este artigo explica como configurar e usar o Selenium WebDriver para testes automatizados.")
                .tipoQa(TipoQa.AUTOMATIZADO)
                .autor("João Silva")
                .dataCriacao(LocalDateTime.now())
                .publicado(true)
                .build();

        Post postSalvo = postRepository.save(post);

        assertThat(postSalvo.getId()).isNotNull();
        assertThat(postSalvo.getTitulo()).isEqualTo("Introdução ao Selenium WebDriver");
    }

    @Test
    @DisplayName("Deve buscar posts por tipo de QA")
    void deveBuscarPostsPorTipoQa() {
        postRepository.save(criarPost("Post Manual 1", TipoQa.MANUAL));
        postRepository.save(criarPost("Post Automatizado 1", TipoQa.AUTOMATIZADO));
        postRepository.save(criarPost("Post Manual 2", TipoQa.MANUAL));

        List<Post> postsManual = postRepository.findByTipoQa(TipoQa.MANUAL);

        assertThat(postsManual).hasSize(2);
        assertThat(postsManual).allMatch(p -> p.getTipoQa() == TipoQa.MANUAL);
    }

    @Test
    @DisplayName("Deve buscar apenas posts publicados")
    void deveBuscarApenasPostsPublicados() {
        postRepository.save(criarPostPublicado("Post 1", true));
        postRepository.save(criarPostPublicado("Post 2", false));
        postRepository.save(criarPostPublicado("Post 3", true));

        List<Post> postsPublicados = postRepository.findByPublicadoTrue();

        assertThat(postsPublicados).hasSize(2);
        assertThat(postsPublicados).allMatch(Post::isPublicado);
    }

    private Post criarPost(String titulo, TipoQa tipoQa) {
        return Post.builder()
                .titulo(titulo)
                .conteudo("Conteúdo de exemplo com mais de 20 caracteres")
                .tipoQa(tipoQa)
                .autor("Autor Teste")
                .dataCriacao(LocalDateTime.now())
                .publicado(true)
                .build();
    }

    private Post criarPostPublicado(String titulo, boolean publicado) {
        return Post.builder()
                .titulo(titulo)
                .conteudo("Conteúdo de exemplo com mais de 20 caracteres")
                .tipoQa(TipoQa.MANUAL)
                .autor("Autor Teste")
                .dataCriacao(LocalDateTime.now())
                .publicado(publicado)
                .build();
    }
}

