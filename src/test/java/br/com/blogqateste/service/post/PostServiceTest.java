package br.com.blogqateste.service.post;

import br.com.blogqateste.dto.post.PostRequestDTO;
import br.com.blogqateste.dto.post.PostResponseDTO;
import br.com.blogqateste.entity.Categoria;
import br.com.blogqateste.entity.Post;
import br.com.blogqateste.entity.Tag;
import br.com.blogqateste.enums.TipoQa;
import br.com.blogqateste.repository.PostRepository;
import br.com.blogqateste.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
 * Testes unitários para PostService — refatorado.
 * Testa os fluxos de criação, atualização, busca e falha.
 */
class PostServiceTest {

    private static final Logger log = LoggerFactory.getLogger(PostServiceTest.class);

    private PostRepository repository;
    private PostService service;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(PostRepository.class);
        service = new PostService(repository);
        log.info("🧩 Mock do PostRepository criado e injetado no PostService.");
    }

    // =========================================================
    // ✅ Criar Post
    // =========================================================
    @Test
    @DisplayName("✅ Deve criar um Post com sucesso e aplicar dados corretamente")
    void deveCriarPostComSucesso() {
        // Arrange
        Categoria categoria = new Categoria("1", "Categoria QA", "Categoria de Testes");
        Tag tag = new Tag("1", "Automação");

        PostRequestDTO dto = new PostRequestDTO(
                null,
                "Post sobre Testes Automatizados",
                "Conteúdo detalhado sobre automação de testes com Spring Boot",
                TipoQa.AUTOMATIZADO,
                "Jean Heberth testes",
                categoria,
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of(tag),
                true
        );

        // Mock save behavior
        when(repository.save(any(Post.class))).thenAnswer(invocation -> {
            Post p = invocation.getArgument(0);
            p.setId("mocked-id");
            log.info("💾 Mock save: {}", p.getTitulo());
            return p;
        });

        // Act
        PostResponseDTO salvo = service.criar(dto);

        // Assert
        assertThat(salvo).isNotNull();
        assertThat(salvo.titulo()).isEqualTo(dto.titulo());
        assertThat(salvo.autor()).isEqualTo(dto.autor());
        assertThat(salvo.publicado()).isTrue();
        verify(repository, times(1)).save(any(Post.class));
    }

    // =========================================================
    // ✅ Atualizar Post
    // =========================================================
    @Test
    @DisplayName("🔄 Deve atualizar um Post existente corretamente")
    void deveAtualizarPostComSucesso() {
        // Arrange
        String id = "123";
        Categoria categoriaAntiga = new Categoria("1", "Antiga", "Categoria antiga");
        Categoria categoriaNova = new Categoria("2", "Nova", "Categoria atualizada");

        Post existente = new Post();
        existente.setId(id);
        existente.setTitulo("Antigo");
        existente.setAutor("Autor Antigo");
        existente.setCategoria(categoriaAntiga);

        when(repository.findById(id)).thenReturn(Optional.of(existente));
        when(repository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Tag tag = new Tag("1", "Automação");

        PostRequestDTO dto = new PostRequestDTO(
                id,
                "Novo título atualizado",
                "Novo conteúdo detalhado de automação",
                TipoQa.AUTOMATIZADO,
                "Jean Heberth",
                categoriaNova,
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of(tag),
                true
        );

        // Act
        PostResponseDTO atualizado = service.atualizar(id, dto);

        // Assert
        assertThat(atualizado).isNotNull();
        assertThat(atualizado.titulo()).isEqualTo(dto.titulo());
        assertThat(atualizado.autor()).isEqualTo(dto.autor());
        assertThat(atualizado.categoria().nome()).isEqualTo("Nova");
        assertThat(atualizado.dataAtualizacao()).isNotNull();
        verify(repository, times(1)).save(any(Post.class));

        log.info("🧠 Post atualizado com sucesso: {}", atualizado.titulo());
    }

    // =========================================================
    // ❌ Atualizar Post inexistente
    // =========================================================
    @Test
    @DisplayName("❌ Deve lançar exceção ao tentar atualizar um Post inexistente")
    void deveLancarExcecaoQuandoPostNaoExistir() {
        // Arrange
        String id = "999";
        when(repository.findById(id)).thenReturn(Optional.empty());

        PostRequestDTO dto = new PostRequestDTO(
                id,
                "Título inexistente",
                "Conteúdo inexistente",
                TipoQa.MANUAL,
                "Jean",
                null,
                null,
                null,
                List.of(),
                false
        );

        // Act + Assert
        assertThatThrownBy(() -> service.atualizar(id, dto))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Post não encontrado");
        verify(repository, never()).save(any());
    }

    // =========================================================
    // 📄 Buscar todos
    // =========================================================
    @Test
    @DisplayName("📄 Deve buscar todos os posts sem loops explícitos")
    void deveBuscarTodosOsPostsSemLoopExplicito() {
        // Arrange
        PageRequest pageable = PageRequest.of(0, 10);
        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(List.of()));

        // Act
        Page<PostResponseDTO> pagina = service.buscarTodos(pageable);

        // Assert
        assertThat(pagina).isNotNull();
        assertThat(pagina.getContent()).isEmpty();
        verify(repository, times(1)).findAll(pageable);

        log.info("📚 Busca retornou {} posts.", pagina.getContent().size());
    }

    // =========================================================
    // 🗑️ Deletar Post
    // =========================================================
    @Test
    @DisplayName("🗑️ Deve deletar um Post existente com sucesso")
    void deveDeletarPostComSucesso() {
        // Arrange
        String id = "456";
        when(repository.existsById(id)).thenReturn(true);

        // Act
        service.deletar(id);

        // Assert
        verify(repository, times(1)).deleteById(id);
        log.info("🗑️ Post ID={} deletado com sucesso.", id);
    }

    @Test
    @DisplayName("🚫 Deve lançar exceção ao tentar deletar Post inexistente")
    void deveLancarExcecaoAoDeletarPostInexistente() {
        // Arrange
        String id = "777";
        when(repository.existsById(id)).thenReturn(false);

        // Act + Assert
        assertThatThrownBy(() -> service.deletar(id))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Post não encontrado");
        verify(repository, never()).deleteById(any());
    }
}
