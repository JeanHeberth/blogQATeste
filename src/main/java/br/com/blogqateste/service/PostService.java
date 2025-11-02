package br.com.blogqateste.service;

import br.com.blogqateste.dto.post.PostRequestDTO;
import br.com.blogqateste.dto.post.PostResponseDTO;
import br.com.blogqateste.entity.Post;
import br.com.blogqateste.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository repository;

    // =========================================================
    // ✅ CREATE
    // =========================================================
    @Transactional
    public PostResponseDTO criar(PostRequestDTO dto) {
        log.info("📝 Criando novo post: {}", dto.titulo());

        Post post = PostRequestDTO.toEntity(dto);
        aplicarCriacao(dto, post);

        Post salvo = repository.save(post);
        log.info("✅ Post criado com ID: {}", salvo.getId());

        return PostResponseDTO.fromEntity(salvo);
    }


    // =========================================================
    // ✅ UPDATE
    // =========================================================
    @Transactional
    public PostResponseDTO atualizar(String id, PostRequestDTO dto) {
        log.info("🔄 Atualizando post com ID: {}", id);

        Post post = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Post não encontrado com ID: " + id));

        aplicarAtualizacao(dto, post);
        Post atualizado = repository.save(post);

        log.info("✅ Post atualizado com sucesso: {}", atualizado.getTitulo());
        return PostResponseDTO.fromEntity(atualizado);
    }


    // =========================================================
    // ✅ FIND ALL
    // =========================================================
    public Page<PostResponseDTO> buscarTodos(Pageable pageable) {
        log.info("📄 Buscando todos os posts (página {}, tamanho {})", pageable.getPageNumber(), pageable.getPageSize());
        return repository.findAll(pageable)
                .map(PostResponseDTO::fromEntity);
    }

    // =========================================================
    // ✅ FIND BY ID
    // =========================================================
    public PostResponseDTO buscarPorId(String id) {
        log.info("🔍 Buscando post por ID: {}", id);
        return repository.findById(id)
                .map(PostResponseDTO::fromEntity)
                .orElseThrow(() -> new NoSuchElementException("Post não encontrado com ID: " + id));
    }

    // =========================================================
    // ✅ DELETE
    // =========================================================
    @Transactional
    public void deletar(String id) {
        log.warn("🗑️ Removendo post com ID: {}", id);
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Post não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }

    private static void aplicarCriacao(PostRequestDTO dto, Post post) {
        post.setTitulo(dto.titulo());
        post.setConteudo(dto.conteudo());
        post.setTipoQa(dto.tipoQa());
        post.setAutor(dto.autor());
        post.setCategoria(dto.categoria());
        post.setTags(dto.tags());
        post.setPublicado(Boolean.TRUE.equals(dto.publicado()));
        post.setDataCriacao(LocalDateTime.now());
        post.setDataAtualizacao(LocalDateTime.now());
    }

    private static void aplicarAtualizacao(PostRequestDTO dto, Post post) {
        if (dto.titulo() != null) post.setTitulo(dto.titulo());
        if (dto.conteudo() != null) post.setConteudo(dto.conteudo());
        if (dto.tipoQa() != null) post.setTipoQa(dto.tipoQa());
        if (dto.autor() != null) post.setAutor(dto.autor());
        if (dto.categoria() != null) post.setCategoria(dto.categoria());
        if (dto.tags() != null) post.setTags(dto.tags());
        if (dto.publicado()) post.setPublicado(true);

        post.setDataAtualizacao(LocalDateTime.now());
    }
}
