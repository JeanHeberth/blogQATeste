package br.com.blogqateste.service;

import br.com.blogqateste.dto.post.PostRequestDTO;
import br.com.blogqateste.dto.post.PostResponseDTO;
import br.com.blogqateste.entity.Categoria;
import br.com.blogqateste.entity.Post;
import br.com.blogqateste.entity.Tag;
import br.com.blogqateste.enums.TipoQa;
import br.com.blogqateste.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para PostService.
 * Usa mocks de PostRepository e verifica comportamento do serviço.
 */
class PostServiceTest {

    private static final Logger log = LoggerFactory.getLogger(PostServiceTest.class);

    private PostRepository repository;
    private PostService service;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(PostRepository.class);
        service = new PostService(repository);
        log.info("🧩 Configuração concluída — mock do PostRepository criado e injetado no PostService.");
    }

    @Test
    @DisplayName("✅ Deve criar um post com sucesso e persistir corretamente")
    void deveCriarPostComSucesso() {
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

        // Mocka o comportamento do save
        when(repository.save(any(Post.class))).thenAnswer(invocation -> {
            Post p = invocation.getArgument(0);
            p.setId("mocked-id");
            log.info("💾 Salvando post mockado: {}", p.getTitulo());
            return p;
        });

        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);

        // Act
        PostResponseDTO salvo = service.criar(dto);

        // Assert
        verify(repository, times(1)).save(captor.capture());
        Post postSalvo = captor.getValue();

        log.info("🧠 Post capturado: título='{}', publicado={}", postSalvo.getTitulo(), postSalvo.isPublicado());

        assertThat(postSalvo.getTitulo()).isEqualTo(dto.titulo());
        assertThat(salvo.publicado()).isTrue();
        assertThat(salvo.titulo()).isEqualTo(dto.titulo());
        assertThat(salvo.tipoQa()).isEqualTo(TipoQa.AUTOMATIZADO);
    }

    @Test
    @DisplayName("🔄 Deve atualizar apenas campos presentes no DTO")
    void deveAtualizarCamposPresentes() {
        // Arrange
        String id = "123";
        Categoria categoria = new Categoria("1", "Categoria QA Atualizado", "Categoria de Testes");
        Tag tag = new Tag("1", "Automação");

        Post existente = new Post();
        existente.setId(id);
        existente.setTitulo("Título Antigo");
        existente.setAutor("Autor Antigo");

        when(repository.findById(id)).thenReturn(Optional.of(existente));
        when(repository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PostRequestDTO dto = new PostRequestDTO(
                id,
                "Novo Título Atualizado",
                "Novo conteúdo detalhado de automação",
                TipoQa.AMBOS,
                "Jean Heberth",
                categoria,
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of(tag),
                true
        );

        // Act
        PostResponseDTO atualizado = service.atualizar(id, dto);

        // Assert
        log.info("🧩 Post atualizado: título='{}', autor='{}'", atualizado.titulo(), atualizado.autor());
        assertThat(atualizado.titulo()).isEqualTo(dto.titulo());
        assertThat(atualizado.dataAtualizacao()).isNotNull();
        verify(repository, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("❌ Deve lançar exceção ao tentar atualizar post inexistente")
    void deveLancarExcecaoQuandoPostNaoExistir() {
        // Arrange
        String id = "No value present";
        when(repository.findById(id)).thenReturn(Optional.empty());

        PostRequestDTO dto = new PostRequestDTO(
                null,
                "Título Inexistente",
                "Conteúdo",
                TipoQa.MANUAL,
                "Autor",
                null,
                null,
                null,
                List.of(),
                false
        );

        // Act & Assert
        log.info("⚠️ Testando exceção para atualização de post inexistente ID={}", id);
        assertThatThrownBy(() -> service.atualizar(id, dto))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining(id);
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("📄 Deve buscar todos os posts sem loops explícitos")
    void deveBuscarTodosSemLoopExplicito() {
        // Arrange
        PageRequest pageable = PageRequest.of(0, 10);
        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(List.of()));

        // Act
        Page<PostResponseDTO> pagina = service.buscarTodos(pageable);

        // Assert
        log.info("📚 Total de posts retornados: {}", pagina.getContent().size());
        assertThat(pagina.getContent()).isEmpty();
        verify(repository, times(1)).findAll(pageable);
    }
}
