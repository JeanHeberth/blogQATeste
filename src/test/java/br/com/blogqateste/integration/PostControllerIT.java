package br.com.blogqateste.integration;

import br.com.blogqateste.config.AbstractIntegrationTest;
import br.com.blogqateste.dto.PostRequestDTO;
import br.com.blogqateste.dto.PostResponseDTO;
import br.com.blogqateste.enums.TipoQa;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(AbstractIntegrationTest.class)
class PostControllerIT extends AbstractIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    @DisplayName("Deve criar um post com sucesso e retornar 201")
    void deveCriarPostComSucesso() {
        PostRequestDTO dto = new PostRequestDTO(
                "1",
                "Título de Teste",
                "Conteúdo de teste com mais de vinte caracteres",
                TipoQa.MANUAL,
                "Jean Heberth",
                LocalDateTime.now(),
                LocalDateTime.now(),
                true
        );

        ResponseEntity<PostResponseDTO> response = restTemplate.postForEntity(
                url("/posts"),
                dto,
                PostResponseDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().titulo()).isEqualTo("Título de Teste");
        assertThat(response.getBody().tipoQa()).isEqualTo(TipoQa.MANUAL);
    }

    @Test
    @DisplayName("Deve buscar todos os posts e retornar 200")
    void deveBuscarTodosOsPosts() {
        PostRequestDTO dto = new PostRequestDTO(
                "1",
                "Post QA",
                "Conteúdo detalhado para o teste de listagem",
                TipoQa.AUTOMATIZADO,
                "Jean Heberth",
                LocalDateTime.now(),
                LocalDateTime.now(),
                true
        );
        restTemplate.postForEntity(url("/posts"), dto, PostResponseDTO.class);

        ResponseEntity<String> response = restTemplate.getForEntity(url("/posts"), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Post QA");
    }

    @Test
    @DisplayName("Deve atualizar um post existente e retornar 200")
    void deveAtualizarPostExistente() {
        PostRequestDTO dtoCriar = new PostRequestDTO(
                "1",
                "Título Original",
                "Conteúdo original do post",
                TipoQa.MANUAL,
                "Jean Heberth",
                LocalDateTime.now(),
                LocalDateTime.now(),
                true
        );
        PostResponseDTO criado = restTemplate.postForEntity(url("/posts"), dtoCriar, PostResponseDTO.class).getBody();

        PostRequestDTO dtoAtualizar = new PostRequestDTO(
                "1",
                "Título Atualizado",
                "Conteúdo atualizado do post",
                TipoQa.AUTOMATIZADO,
                "Jean Heberth",
                LocalDateTime.now(),
                LocalDateTime.now(),
                true
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PostRequestDTO> request = new HttpEntity<>(dtoAtualizar, headers);

        ResponseEntity<PostResponseDTO> response = restTemplate.exchange(
                url("/posts/" + criado.id()),
                HttpMethod.PUT,
                request,
                PostResponseDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().titulo()).isEqualTo("Título Atualizado");
    }

    @Test
    @DisplayName("Deve deletar um post e retornar 204")
    void deveDeletarPost() {
        PostRequestDTO dto = new PostRequestDTO(
                "1",
                "Título para Deletar",
                "Conteúdo de teste para exclusão",
                TipoQa.MANUAL,
                "Jean Heberth",
                LocalDateTime.now(),
                LocalDateTime.now(),
                true
        );
        PostResponseDTO criado = restTemplate.postForEntity(url("/posts"), dto, PostResponseDTO.class).getBody();

        ResponseEntity<Void> response = restTemplate.exchange(
                url("/posts/" + criado.id()),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
