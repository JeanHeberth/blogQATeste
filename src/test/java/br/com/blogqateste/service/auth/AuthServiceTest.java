package br.com.blogqateste.service.auth;

import br.com.blogqateste.dto.auth.AuthRequest;
import br.com.blogqateste.dto.auth.AuthResponse;
import br.com.blogqateste.dto.register.RegisterRequest;
import br.com.blogqateste.entity.Usuario;
import br.com.blogqateste.enums.UsuarioRole;
import br.com.blogqateste.repository.UsuarioRepository;
import br.com.blogqateste.service.AuthService;
import br.com.blogqateste.config.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // =========================================================
    // ✅ Cenário 1 — Registro de novo usuário com sucesso
    // =========================================================
    @Test
    @DisplayName("Deve registrar novo usuário e retornar token JWT")
    void deveRegistrarUsuarioComSucesso() {
        // Arrange
        RegisterRequest req = new RegisterRequest("Jean QA", "jean@test.com", "123456", UsuarioRole.AUTOR);

        when(usuarioRepository.existsByEmail(req.email())).thenReturn(false);
        when(passwordEncoder.encode(req.senha())).thenReturn("encoded123");
        when(jwtUtil.gerarToken(req.email())).thenReturn("fake-jwt-token");
        when(usuarioRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0)); // ✅ Aqui está o fix

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);

        // Act
        AuthResponse response = authService.register(req);

        // Assert
        verify(usuarioRepository).save(captor.capture());
        Usuario salvo = captor.getValue();

        assertThat(salvo.getNome()).isEqualTo("Jean QA");
        assertThat(salvo.getSenha()).isEqualTo("encoded123");
        assertThat(response.token()).isEqualTo("fake-jwt-token");
        assertThat(response.role()).isEqualTo(UsuarioRole.AUTOR);
    }


    // =========================================================
    // ❌ Cenário 2 — Tentativa de registro com e-mail duplicado
    // =========================================================
    @Test
    @DisplayName("Deve lançar exceção ao tentar registrar e-mail duplicado")
    void deveLancarExcecaoAoRegistrarEmailDuplicado() {
        // Arrange
        RegisterRequest req = new RegisterRequest("Jean QA", "duplicado@test.com", "senha123", UsuarioRole.AUTOR);
        when(usuarioRepository.existsByEmail(req.email())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> authService.register(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email já cadastrado");

        verify(usuarioRepository, never()).save(any());
    }

    // =========================================================
    // 🔑 Cenário 3 — Login com sucesso
    // =========================================================
    @Test
    @DisplayName("Deve realizar login com sucesso e retornar token JWT")
    void deveLogarComSucesso() {
        // Arrange
        AuthRequest req = new AuthRequest("login@test.com", "senha123");
        Usuario usuario = Usuario.builder()
                .id("1")
                .nome("Login Teste")
                .email(req.email())
                .senha("encoded123")
                .role(UsuarioRole.AUTOR)
                .build();

        when(usuarioRepository.findByEmail(req.email())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(req.senha(), usuario.getSenha())).thenReturn(true);
        when(jwtUtil.gerarToken(req.email())).thenReturn("jwt-token");

        // Act
        AuthResponse response = authService.login(req);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.email()).isEqualTo(req.email());
        assertThat(response.token()).isEqualTo("jwt-token");
        verify(usuarioRepository).findByEmail(req.email());
    }

    // =========================================================
    // ❌ Cenário 4 — Login com senha incorreta
    // =========================================================
    @Test
    @DisplayName("Deve lançar exceção ao tentar login com senha incorreta")
    void deveFalharLoginSenhaIncorreta() {
        // Arrange
        AuthRequest req = new AuthRequest("fail@test.com", "wrong");
        Usuario usuario = Usuario.builder()
                .email(req.email())
                .senha("encoded123")
                .build();

        when(usuarioRepository.findByEmail(req.email())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(req.senha(), usuario.getSenha())).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> authService.login(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Credenciais inválidas");
    }

    // =========================================================
    // ❌ Cenário 5 — Login com usuário inexistente
    // =========================================================
    @Test
    @DisplayName("Deve lançar exceção ao tentar login com usuário inexistente")
    void deveFalharLoginUsuarioInexistente() {
        // Arrange
        AuthRequest req = new AuthRequest("naoexiste@test.com", "123456");
        when(usuarioRepository.findByEmail(req.email())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> authService.login(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Credenciais inválidas");
    }
}

