package br.com.blogqateste.service;

import br.com.blogqateste.dto.autor.AutorRequestDTO;
import br.com.blogqateste.dto.autor.AutorResponseDTO;
import br.com.blogqateste.entity.Autor;
import br.com.blogqateste.repository.AutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AutorService {

    private final AutorRepository repository;

    @Transactional
    public AutorResponseDTO criar(AutorRequestDTO dto) {
        Autor autor = AutorRequestDTO.toEntity(dto);
        Autor salvo = repository.save(autor);
        return AutorResponseDTO.fromEntity(salvo);
    }

    @Transactional
    public AutorResponseDTO atualizar(String id, AutorRequestDTO dto) {
        Autor existente = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Autor não encontrado: " + id));

        existente.setNome(dto.nome());
        existente.setEmail(dto.email());
        existente.setBio(dto.bio());
        existente.setFotoUrl(dto.fotoUrl());

        Autor atualizado = repository.save(existente);
        return AutorResponseDTO.fromEntity(atualizado);
    }

    @Transactional(readOnly = true)
    public List<AutorResponseDTO> buscarTodos() {
        return repository.findAll()
                .stream()
                .map(AutorResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public AutorResponseDTO buscarPorId(String id) {
        Autor autor = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Autor não encontrado: " + id));
        return AutorResponseDTO.fromEntity(autor);
    }

    @Transactional
    public void deletar(String id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Autor não encontrado: " + id);
        }
        repository.deleteById(id);
    }
}

