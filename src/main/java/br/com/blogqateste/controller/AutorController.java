package br.com.blogqateste.controller;

import br.com.blogqateste.entity.Autor;
import br.com.blogqateste.entity.Categoria;
import br.com.blogqateste.repository.AutorRepository;
import br.com.blogqateste.repository.CategoriaRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor
public class AutorController {

    private final AutorRepository autorRepository;

    @GetMapping
    public ResponseEntity<List<Autor>> listar() {
        return ResponseEntity.ok(autorRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Autor> criar(@Valid @RequestBody Autor autor) {
        return ResponseEntity.ok(autorRepository.save(autor));
    }
}

