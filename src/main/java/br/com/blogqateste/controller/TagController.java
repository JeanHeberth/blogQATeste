package br.com.blogqateste.controller;

import br.com.blogqateste.entity.Categoria;
import br.com.blogqateste.entity.Tag;
import br.com.blogqateste.repository.TagRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagRepository tagRepository;

    @GetMapping
    public ResponseEntity<List<Tag>> listar() {
        return ResponseEntity.ok(tagRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Tag> criar(@Valid @RequestBody Tag tag) {
        return ResponseEntity.ok(tagRepository.save(tag));
    }
}

