package br.com.blogqateste.service;

import br.com.blogqateste.dto.PostRequestDTO;
import br.com.blogqateste.dto.PostResponseDTO;
import br.com.blogqateste.entity.Post;
import br.com.blogqateste.enums.TipoQa;
import br.com.blogqateste.exception.PostNotFoundException;
import br.com.blogqateste.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceTest {

    private PostRepository repository;
    private PostService service;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(PostRepository.class);
        service = new PostService(repository);
    }

    @Test
    @DisplayName("Deve criar um post com sucesso")
    void criar() {
        PostRequestDTO dto = new PostRequestDTO(
                "1",
                "Conteúdo com mais de vinte caracteres",
                "Teste manual teste",
                TipoQa.AMBOS,
                "Jean Heberth",
                LocalDateTime.now(),
                LocalDateTime.now(),
                true
        );

        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        PostResponseDTO salvo = service.criar(dto);

        verify(repository).save(captor.capture());
        Post enviado = captor.getValue();

        assertThat(enviado.getTitulo()).isEqualTo("Conteúdo com mais de vinte caracteres");
        assertThat(salvo.publicado()).isTrue();
    }

    @Test
    @DisplayName("Deve atualizar campos presentes no DTO")
    void atualizar() {
        String id = "123";
        Post existente = new Post();
        existente.setId(id);
        existente.setTitulo("Old");
        when(repository.findById(id)).thenReturn(Optional.of(existente));
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        PostRequestDTO dto = new PostRequestDTO(
                "2",
                "Conteúdo com mais de vinte caracteres 2",
                "Teste manual teste 2",
                TipoQa.AMBOS,
                "Jean Heberth 2",
                LocalDateTime.now(),
                LocalDateTime.now(),
                true
        );

        PostResponseDTO atualizado = service.atualizar(id, dto);

        assertThat(atualizado.titulo()).isEqualTo("Conteúdo com mais de vinte caracteres 2");
        assertThat(atualizado.dataAtualizacao()).isNotNull();
    }

    @Test
    @DisplayName("Deve lançar exceção quando post não existir")
    void atualizarNotFound() {
        String id = "No value present";
        when(repository.findById(id)).thenReturn(Optional.empty());
        PostRequestDTO dto = new PostRequestDTO(
                null,
                null,
                null,
                null,
                null,
                null,
                null, false);
        assertThatThrownBy(() -> service.atualizar(id, dto))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining(id);
    }

    @Test
    @DisplayName("Deve buscar todos sem loop explícito")
    void buscarTodos() {
        PageRequest pr = PageRequest.of(0, 10);
        when(repository.findAll(pr)).thenReturn(new PageImpl<>(List.of()));
        Page<PostResponseDTO> pagina = service.buscarTodos(pr);
        assertThat(pagina.getContent()).isEmpty();
    }
}
