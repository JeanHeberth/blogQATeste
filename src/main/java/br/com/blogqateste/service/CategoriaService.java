package br.com.blogqateste.service;

import br.com.blogqateste.dto.categoria.CategoriaRequestDTO;
import br.com.blogqateste.dto.categoria.CategoriaResponseDTO;
import br.com.blogqateste.entity.Categoria;
import br.com.blogqateste.exception.ResourceNotFoundException;
import br.com.blogqateste.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;


    @Transactional
    public CategoriaResponseDTO criar(CategoriaRequestDTO dto){
        Categoria categoria = CategoriaRequestDTO.toEntity(dto);
        Categoria salvo = categoriaRepository.save(categoria);
        return CategoriaResponseDTO.fromEntity(salvo);
    }

    @Transactional
    public CategoriaResponseDTO atualizar(String id, CategoriaRequestDTO dto){
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada: " + id));
        categoria.setNome(dto.nome());
        categoria.setDescricao(dto.descricao());
        Categoria salvo = categoriaRepository.save(categoria);
        return CategoriaResponseDTO.fromEntity(salvo);
    }

    @Transactional(readOnly = true)
    public CategoriaResponseDTO buscarPorId(String id){
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada: " + id));
        return CategoriaResponseDTO.fromEntity(categoria);
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> buscarTodos(){
        List<Categoria> categorias = categoriaRepository.findAll();
        return categorias.stream().map(CategoriaResponseDTO::fromEntity).toList();
    }

    @Transactional
    public void deletar(String id){
        categoriaRepository.deleteById(id);
    }
}
