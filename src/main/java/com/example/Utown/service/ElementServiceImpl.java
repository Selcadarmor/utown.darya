package com.example.Utown.service;

import com.example.Utown.dto.elementDTO.ElementDto;
import com.example.Utown.dto.elementDTO.ElementInfoDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.ElementMapper;
import com.example.Utown.model.Element;
import com.example.Utown.model.Option;
import com.example.Utown.repository.ElementRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ElementServiceImpl implements ElementService {

    private final ElementRepository elementRepository;
    private final ElementMapper elementMapper;

    @Override
    public Element getById(Long id) {
        Element element = elementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Element", id));
        return element;
    }

    @Override
    public Element create(ElementDto dto) {

        Element element = elementMapper.toEntity(dto);
        return elementRepository.save(element);
    }

    @Override
    @Transactional
    public Set<Element> createElementsForOption(Option option, Set<ElementInfoDto> elementDtos) {
        if (elementDtos == null || elementDtos.isEmpty()) return Collections.emptySet();

        return elementDtos.stream().map(dto -> Element.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .isActive(true)
                .isDeleted(false)
                .option(option)
                .build()
        ).collect(Collectors.toSet());
    }

    @Override
    public Element update(Long id, ElementDto dto) {
        Element element = getById(id);

        element.setName(dto.getName());
        element.setDescription(dto.getDescription());
        element.setPrice(dto.getPrice());
        element.setIsActive(dto.getIsActive());
        element.setIsDeleted(dto.getIsDeleted());

        return elementRepository.save(element);
    }

    @Override
    @Transactional
    public Set<Element> updateElementsForOption(Option option, Set<ElementInfoDto> elementDtos) {
        Map<Long, Element> existing = option.getElements().stream()
                .collect(Collectors.toMap(Element::getId, Function.identity()));

        Set<Element> result = new HashSet<>();

        for (ElementInfoDto dto : elementDtos) {
            Element element = dto.getId() != null && existing.containsKey(dto.getId())
                    ? existing.remove(dto.getId())
                    : new Element();

            element.setName(dto.getName());
            element.setPrice(dto.getPrice());
            element.setDescription(dto.getDescription());
            element.setIsActive(true);
            element.setOption(option);

            result.add(element);
        }

        return result;
    }

    @Override
    public void deleteElement(Long id) {
        Element element = getById(id);
         element.setIsDeleted(true);
         elementRepository.save(element);
    }
}

