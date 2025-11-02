package br.com.blogqateste.service;

import br.com.blogqateste.dto.tag.TagRequestDTO;
import br.com.blogqateste.dto.tag.TagResponseDTO;
import br.com.blogqateste.entity.Tag;
import br.com.blogqateste.exception.ResourceNotFoundException;
import br.com.blogqateste.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private  final TagRepository tagRepository;


    @Transactional
    public TagResponseDTO criar(TagRequestDTO dto){
        Tag tag = TagRequestDTO.toEntity(dto);
        Tag salvo = tagRepository.save(tag);
        return TagResponseDTO.fromEntity(salvo);
    }

    @Transactional
    public TagResponseDTO atualizar(String id, TagRequestDTO dto){
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag não encontrada: " + id));
        tag.setNome(dto.nome());
        Tag salvo = tagRepository.save(tag);
        return TagResponseDTO.fromEntity(salvo);
    }

    @Transactional(readOnly = true)
    public TagResponseDTO buscarPorId(String id){
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag não encontrada: " + id));
        return TagResponseDTO.fromEntity(tag);
    }

    @Transactional(readOnly = true)
    public List<TagResponseDTO> buscarTodos(){
        List<Tag> tags = tagRepository.findAll();
        return tags.stream().map(TagResponseDTO::fromEntity).toList();
    }

    @Transactional
    public void deletar(String id){
        tagRepository.deleteById(id);
    }
}
