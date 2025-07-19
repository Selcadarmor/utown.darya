package com.example.Utown.mapper;

import com.example.Utown.dto.elementDTO.ElementDto;
import com.example.Utown.model.Element;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ElementMapper {

    ElementDto toDto(Element element);
    
    Element toEntity(ElementDto dto);
}

