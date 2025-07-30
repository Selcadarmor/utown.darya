package com.example.Utown.service;

import com.example.Utown.dto.elementDTO.ElementDto;
import com.example.Utown.dto.elementDTO.ElementInfoDto;
import com.example.Utown.model.Element;
import com.example.Utown.model.Option;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface ElementService {
    Element getById(Long id);
    List<Element> getElementsByIds(List<Long> ids);
    Element create(ElementDto dto);
    Set<Element> createElementsForOption(Option option, Set<ElementInfoDto> elementDtos);
    BigDecimal calculateElementsPrice(List<Long> elementIds);
    Element update(Long id, ElementDto dto);
    Set<Element> updateElementsForOption(Option option, Set<ElementInfoDto> elementDtos);
    void delete(Long id);
}

