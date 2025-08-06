package com.example.Utown.mapper;

import com.example.Utown.dto.elementDTO.ElementDto;
import com.example.Utown.model.Element;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ElementMapper {

    ElementDto toDto(Element element);

    @Mapping(target = "option", ignore = true)
    Element toEntity(ElementDto dto);
}
