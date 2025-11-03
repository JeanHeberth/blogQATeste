package br.com.blogqateste.dto.auth;

import br.com.blogqateste.enums.UsuarioRole;

public record AuthResponse(
        String userId,
        String nome,
        String email,
        UsuarioRole role,
        String token,
        String message
) {

    // ✅ Retorno ao registrar novo usuário
    public static AuthResponse registered(String userId, String nome, String email, UsuarioRole role, String token) {
        return new AuthResponse(userId, nome, email, role, token, "Usuário registrado com sucesso.");
    }

    // ✅ Retorno ao realizar login
    public static AuthResponse ok(String userId, String nome, String email, UsuarioRole role, String token) {
        return new AuthResponse(userId, nome, email, role, token, "Login realizado com sucesso.");
    }

    // ✅ Retorno genérico (sem token)
    public static AuthResponse error(String message) {
        return new AuthResponse(null, null, null, null, null, message);
    }
}
