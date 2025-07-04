package com.example.Utown.service;

import com.example.Utown.dto.elementDTO.ElementDto;
import com.example.Utown.model.Element;

import java.util.List;

public interface ElementService {
    Element create(ElementDto dto);
    ElementDto getById(Long id);
    List<ElementDto> getAll();
    Element update(Long id, ElementDto dto);
    void delete(Long id);
}

