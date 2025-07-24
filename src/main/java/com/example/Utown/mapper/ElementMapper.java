package com.example.Utown.mapper;

import com.example.Utown.dto.elementDTO.ElementDto;
import com.example.Utown.dto.elementDTO.ElementForClientDto;
import com.example.Utown.dto.elementDTO.ElementInfoDto;
import com.example.Utown.model.Element;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface ElementMapper {

    ElementDto toDto(Element element);
    
    Element toEntity(ElementDto dto);

    @Mapping(source = "element.id", target = "id")
    @Mapping(source = "element.name", target = "name")
    @Mapping(source = "element.price", target = "price")
    ElementInfoDto mapElement(Element element);

    Set<ElementDto> toElementDtoList(Set<Element> elements);
    Set<ElementDto> toElementDtoSet(Set<Element> elements);

    ElementForClientDto elementToClientDto(Element element);
}