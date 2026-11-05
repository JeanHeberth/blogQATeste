package br.com.blogqateste.integration.auth;


import br.com.blogqateste.integration.config.AbstractIntegrationTest;
import br.com.blogqateste.dto.auth.AuthRequest;
import br.com.blogqateste.dto.auth.AuthResponse;
import br.com.blogqateste.dto.register.RegisterRequest;
import br.com.blogqateste.enums.UsuarioRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@ActiveProfiles("test")
class AuthControllerIT extends AbstractIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    // ============================================================
    // 🧩 Cenário 1 — Registrar novo usuário com sucesso
    // ============================================================
    @Test
    @DisplayName("Deve registrar um novo usuário com sucesso e retornar token JWT")
    void deveRegistrarUsuarioComSucesso() {
        // Arrange
        RegisterRequest request = new RegisterRequest(
                "Jean Heberth",
                "jean.it@test.com",
                "123456",
                UsuarioRole.AUTOR
        );

        // Act
        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                url("/auth/register"),
                request,
                AuthResponse.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        AuthResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.token()).isNotBlank();
        assertThat(body.email()).isEqualTo(request.email());
        assertThat(body.message()).contains("sucesso");
    }

    // ============================================================
    // 🔑 Cenário 2 — Login após registro
    // ============================================================
    @Test
    @DisplayName("Deve realizar login e retornar token JWT válido")
    void deveRealizarLoginComSucesso() {
        // Arrange — registra antes de logar
        RegisterRequest register = new RegisterRequest(
                "Jessica QA",
                "jessica.qa@test.com",
                "senha123",
                UsuarioRole.AUTOR
        );
        restTemplate.postForEntity(url("/auth/register"), register, AuthResponse.class);

        AuthRequest login = new AuthRequest(
                register.email(),
                register.senha()
        );

        // Act
        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                url("/auth/login"),
                login,
                AuthResponse.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        AuthResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.email()).isEqualTo(register.email());
        assertThat(body.token()).isNotBlank();
        assertThat(body.message()).contains("sucesso");
    }

    // ============================================================
    // ❌ Cenário 3 — Login com credenciais inválidas
    // ============================================================
    @Test
    @DisplayName("Deve retornar erro 401 ao tentar login com credenciais inválidas")
    void deveFalharLoginComCredenciaisInvalidas() {
        // Arrange
        AuthRequest request = new AuthRequest("inexistente@test.com", "senhaerrada");

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(
                url("/auth/login"),
                request,
                String.class // ✅ Aqui é o segredo
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED)
                .withFailMessage("Esperava erro 401 para login inválido");
        assertThat(response.getBody()).contains("Credenciais inválidas");
    }

}

