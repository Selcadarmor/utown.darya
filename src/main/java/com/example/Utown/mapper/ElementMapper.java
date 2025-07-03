package com.example.Utown.mapper;

import com.example.Utown.dto.elementDTO.ElementDto;
import com.example.Utown.model.Element;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ElementMapper {

    @Mapping(source = "file.id", target = "fileId")
    ElementDto toDto(Element element);

    @Mapping(source = "fileId", target = "file.id")
    Element toEntity(ElementDto dto);
}

