package com.example.Utown.mapper;

import com.example.Utown.dto.optionDTO.OptionDto;
import com.example.Utown.model.Option;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OptionMapper {

    OptionDto optionToDto(Option option);

    Option optionDtoToEntity(OptionDto dto);
}

