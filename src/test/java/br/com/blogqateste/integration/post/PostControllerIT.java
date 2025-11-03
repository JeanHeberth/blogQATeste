package br.com.blogqateste.integration.post;

import br.com.blogqateste.controller.PostController;
import br.com.blogqateste.dto.post.PostRequestDTO;
import br.com.blogqateste.dto.post.PostResponseDTO;
import br.com.blogqateste.entity.Categoria;
import br.com.blogqateste.entity.Tag;
import br.com.blogqateste.enums.TipoQa;
import br.com.blogqateste.integration.config.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@TestExecutionListeners(
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS,
        listeners = {
                org.springframework.test.context.support.DependencyInjectionTestExecutionListener.class,
                org.springframework.test.context.support.DirtiesContextTestExecutionListener.class
        }
)
class PostControllerIT extends AbstractIntegrationTest {

    @Autowired
    private PostController postController;

    @Test
    @DisplayName("Deve criar um Post com sucesso via Controller e retornar o DTO completo")
    void deveCriarPostComSucessoViaController() {
        System.out.println("🟦 Iniciando teste de criação de Post via Controller...");

        // Arrange
        Categoria categoria = new Categoria("1", "Categoria QA", "Categoria de Testes");
        Tag tag = new Tag("1", "Automação");

        PostRequestDTO dto = new PostRequestDTO(
                null,
                "Post sobre Testes Automatizados",
                "Conteúdo detalhado sobre automação de testes com Spring Boot e boas práticas",
                TipoQa.AUTOMATIZADO,
                "Jean Heberth",
                categoria,
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of(tag),
                true
        );


        // Act
        var responseEntity = postController.criar(dto);
        assertThat(responseEntity).isNotNull();

        PostResponseDTO response = responseEntity.getBody();
        assertThat(response).isNotNull();

        // Assert
        assertThat(response.titulo()).isEqualTo(dto.titulo());
        assertThat(response.autor()).isEqualTo(dto.autor());
        assertThat(response.tipoQa()).isEqualTo(dto.tipoQa());
        assertThat(response.categoria().nome()).isEqualTo("Categoria QA");
        assertThat(response.tags()).hasSize(1);
        assertThat(response.publicado()).isTrue();
    }

    @Test
    @DisplayName("Deve atualizar um Post com sucesso via Controller e retornar o DTO completo")
    void deveAtualizarPostComSucessoViaController() {
        System.out.println("🟦 Iniciando teste de criação de Post via Controller...");

        // Arrange
        Categoria categoria = new Categoria("1", "Categoria QA Atualizado", "Categoria de Testes");
        Tag tag = new Tag("1", "Automação");

        PostRequestDTO dto = new PostRequestDTO(
                null,
                "Post sobre Testes Automatizados automatizado",
                "Conteúdo detalhado sobre automação de testes com Spring Boot e boas práticas",
                TipoQa.AUTOMATIZADO,
                "Jean Heberth",
                categoria,
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of(tag),
                true
        );


        var responseEntity = postController.criar(dto);
        PostResponseDTO response = responseEntity.getBody();


        // Assert
        assertThat(response).isNotNull();
        assertThat(response.titulo()).isEqualTo(dto.titulo());
        assertThat(response.autor()).isEqualTo(dto.autor());
        assertThat(response.tipoQa()).isEqualTo(dto.tipoQa());
        assertThat(response.categoria().nome()).isEqualTo("Categoria QA Atualizado");
        assertThat(response.tags()).hasSize(1);
        assertThat(response.publicado()).isTrue();
    }
}
