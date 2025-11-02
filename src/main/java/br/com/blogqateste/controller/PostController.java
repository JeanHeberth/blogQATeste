package br.com.blogqateste.controller;

import br.com.blogqateste.dto.post.PostRequestDTO;
import br.com.blogqateste.dto.post.PostResponseDTO;
import br.com.blogqateste.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponseDTO> criar(@RequestBody @Validated PostRequestDTO dto) {
        PostResponseDTO postCriado = postService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(postCriado);

    }

    @GetMapping
    public ResponseEntity<Page<PostResponseDTO>> buscarTodos(Pageable pageable) {
        return ResponseEntity.ok(postService.buscarTodos(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(postService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDTO> atualizar(@PathVariable String id, @RequestBody @Validated PostRequestDTO dto) {
        PostResponseDTO postAtualizado = postService.atualizar(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(postAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        postService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
