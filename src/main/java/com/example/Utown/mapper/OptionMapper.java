package com.example.Utown.mapper;

import com.example.Utown.dto.optionDTO.OptionDto;
import com.example.Utown.dto.optionDTO.OptionForClientDto;
import com.example.Utown.dto.optionDTO.OptionInfoDto;
import com.example.Utown.model.Option;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ElementMapper.class})
public interface OptionMapper {

    OptionDto optionToDto(Option option);

    Option optionDtoToEntity(OptionDto dto);

    @Mapping(source = "elements", target = "elements")
    OptionInfoDto mapToOption(Option option);

    OptionForClientDto optionToClientDto(Option option);
}
