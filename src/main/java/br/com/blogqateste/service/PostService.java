package br.com.blogqateste.service;

import br.com.blogqateste.dto.PostRequestDTO;
import br.com.blogqateste.dto.PostResponseDTO;
import br.com.blogqateste.entity.Post;
import br.com.blogqateste.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository repository;


    @Transactional
    public PostResponseDTO criar(PostRequestDTO dto) {
        Post post = new Post();
        aplicarCriacao(dto, post);
       Post postCriado = repository.save(post);
       return PostResponseDTO.fromEntity(postCriado);

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