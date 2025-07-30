package com.example.Utown.service;

import com.example.Utown.dto.elementDTO.ElementDto;
import com.example.Utown.dto.elementDTO.ElementInfoDto;
import com.example.Utown.model.Element;
import com.example.Utown.model.Option;

import java.util.List;
import java.util.Set;

public interface ElementService {
    Element getById(Long id);
    Element create(ElementDto dto);
    Set<Element> createElementsForOption(Option option, Set<ElementInfoDto> elementDtos);
    Element update(Long id, ElementDto dto);
    Set<Element> updateElementsForOption(Option option, Set<ElementInfoDto> elementDtos);
    void deleteElement(Long id);
}

