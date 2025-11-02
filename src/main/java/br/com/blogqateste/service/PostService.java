package br.com.blogqateste.service;

import br.com.blogqateste.dto.post.PostRequestDTO;
import br.com.blogqateste.dto.post.PostResponseDTO;
import br.com.blogqateste.entity.Categoria;
import br.com.blogqateste.entity.Post;
import br.com.blogqateste.entity.Tag;
import br.com.blogqateste.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository repository;


    private static final Logger log = LoggerFactory.getLogger(PostService.class);

    @Transactional
    public PostResponseDTO criar(PostRequestDTO dto) {
        log.info("📝 Criando novo post: {}", dto.titulo());

        Post post = PostRequestDTO.toEntity(dto);
        aplicarCriacao(dto, post);

        Post salvo = repository.save(post);
        log.info("✅ Post criado com ID: {}", salvo.getId());

        return PostResponseDTO.fromEntity(salvo);
    }


    @Transactional
    public PostResponseDTO atualizar(String id, PostRequestDTO dto) {
        Post post = repository.findById(id).orElseThrow();
        aplicarAtualizacao(dto, post);
        Post postCriado = repository.save(post);
        return PostResponseDTO.fromEntity(postCriado);
    }

    @Transactional(readOnly = true)
    public Page<PostResponseDTO> buscarTodos(Pageable pageable) {
        return repository.findAll(pageable).map(PostResponseDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public PostResponseDTO buscarPorId(String id) {
        return PostResponseDTO.fromEntity(repository.findById(id).orElseThrow());
    }

    @Transactional(readOnly = true)
    public void deletar(String id) {
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

    private void aplicarAtualizacao(PostRequestDTO dto, Post post) {
        if (dto.titulo() != null) post.setTitulo(dto.titulo());
        if (dto.conteudo() != null) post.setConteudo(dto.conteudo());
        if (dto.tipoQa() != null) post.setTipoQa(dto.tipoQa());
        if (dto.autor() != null) post.setAutor(dto.autor());
        if (dto.categoria() != null) post.setCategoria(dto.categoria());
        if (dto.tags() != null) post.setTags(dto.tags());
        post.setDataAtualizacao(LocalDateTime.now());
    }

}