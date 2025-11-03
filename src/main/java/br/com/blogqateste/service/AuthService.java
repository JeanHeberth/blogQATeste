package br.com.blogqateste.service;

import br.com.blogqateste.config.JwtUtil;
import br.com.blogqateste.dto.auth.AuthRequest;
import br.com.blogqateste.dto.auth.AuthResponse;
import br.com.blogqateste.dto.register.RegisterRequest;
import br.com.blogqateste.entity.Usuario;
import br.com.blogqateste.enums.UsuarioRole;
import br.com.blogqateste.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // ============================================================
    // 🔐 REGISTRO
    // ============================================================
    @Transactional
    public AuthResponse register(RegisterRequest req) {
        log.info("📝 Registrando novo usuário: {}", req.email());

        if (usuarioRepository.existsByEmail(req.email())) {
            log.warn("❌ Tentativa de registro com email já existente: {}", req.email());
            throw new IllegalArgumentException("Email já cadastrado.");
        }

        Usuario usuario = Usuario.builder()
                .nome(req.nome())
                .email(req.email())
                .senha(passwordEncoder.encode(req.senha()))
                .role(req.role() != null ? req.role() : UsuarioRole.AUTOR)
                .build();

        Usuario salvo = usuarioRepository.save(usuario);

        // ✅ Gera token automaticamente no registro (opcional, mas útil)
        String token = jwtUtil.gerarToken(salvo.getEmail());

        log.info("✅ Usuário registrado com sucesso: {} (Role: {})", salvo.getEmail(), salvo.getRole());
        return AuthResponse.registered(
                salvo.getId(),
                salvo.getNome(),
                salvo.getEmail(),
                salvo.getRole(),
                token
        );
    }

    // ============================================================
    // 🔑 LOGIN
    // ============================================================
    public AuthResponse login(AuthRequest req) {
        log.info("🔐 Efetuando login: {}", req.email());


        Usuario usuario = usuarioRepository.findByEmail(req.email())
                .orElseThrow(() -> {
                    log.warn("❌ Login falhou — email não encontrado: {}", req.email());
                    return new IllegalArgumentException("Credenciais inválidas.");
                });

        if (!passwordEncoder.matches(req.senha(), usuario.getSenha())) {
            log.warn("❌ Senha incorreta para o email: {}", req.email());
            throw new IllegalArgumentException("Credenciais inválidas.");
        }

        // ✅ Gera JWT válido
        String token = jwtUtil.gerarToken(usuario.getEmail());

        log.info("✅ Login bem-sucedido para o usuário: {}", usuario.getEmail());
        return AuthResponse.ok(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole(),
                token
        );
    }
}
