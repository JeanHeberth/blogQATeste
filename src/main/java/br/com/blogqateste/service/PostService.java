package br.com.blogqateste.service;

import br.com.blogqateste.dto.PostRequestDTO;
import br.com.blogqateste.entity.Post;
import br.com.blogqateste.exception.PostNotFoundException;
import br.com.blogqateste.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PostService {

    private final PostRepository repository;

    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Post criar(PostRequestDTO dto) {
        var post = new Post();
        aplicarCriacao(dto, post);
        return repository.save(post);
    }

    @Transactional
    public Post atualizar(String id, PostRequestDTO dto) {
        Post post = repository.findById(id).orElseThrow();
        aplicarAtualizacao(dto, post);
        return repository.save(post);
    }

    @Transactional(readOnly = true)
    public Page<Post> buscarTodos(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Post buscarPorId(String id) {
        return repository.findById(id).orElseThrow();
    }

    /* Helpers ------------------------------------------------------------- */

    private static void aplicarCriacao(PostRequestDTO dto, Post post) {
        post.setTitulo(dto.titulo());
        post.setConteudo(dto.conteudo());
        post.setTipoQa(dto.tipoQa());
        post.setAutor(dto.autor());
        post.setDataCriacao(LocalDateTime.now());
        post.setPublicado(Boolean.TRUE.equals(dto.publicado()));
    }

    private static void aplicarAtualizacao(PostRequestDTO dto, Post post) {
        if (dto.titulo() != null) post.setTitulo(dto.titulo());
        if (dto.conteudo() != null) post.setConteudo(dto.conteudo());
        if (dto.tipoQa() != null) post.setTipoQa(dto.tipoQa());
        if (dto.autor() != null) post.setAutor(dto.autor());
        post.setPublicado(dto.publicado());
        post.setDataAtualizacao(LocalDateTime.now());
    }
}