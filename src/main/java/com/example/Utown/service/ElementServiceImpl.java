package com.example.Utown.service;

import com.example.Utown.dto.elementDTO.ElementDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.ElementMapper;
import com.example.Utown.model.Element;
import com.example.Utown.repository.ElementRepository;
import com.example.Utown.repository.FileInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ElementServiceImpl implements ElementService {

    private final ElementRepository elementRepository;
    private final FileInfoRepository fileInfoRepository;
    private final ElementMapper elementMapper;

    @Override
    public Element create(ElementDto dto) {

        Element element = elementMapper.toEntity(dto);
        return elementRepository.save(element);
    }

    @Override
    public ElementDto getById(Long id) {
        Element element = elementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Element not found", id));
        return elementMapper.toDto(element);
    }

    @Override
    public List<ElementDto> getAll() {
        return elementRepository.findAll().stream()
                .map(elementMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Element update(Long id, ElementDto dto) {
        Element element = elementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Element not found", id));

        element.setName(dto.getName());
        element.setDescription(dto.getDescription());
        element.setPrice(dto.getPrice());
        element.setIsActive(dto.getIsActive());
        element.setIsDeleted(dto.getIsDeleted());

        return elementRepository.save(element);
    }

    @Override
    public void delete(Long id) {
        Element element = elementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Element not found", id));
        elementRepository.delete(element);
    }
}

