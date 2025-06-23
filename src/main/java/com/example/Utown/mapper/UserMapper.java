package com.example.Utown.mapper;

import com.example.Utown.dto.otherDto.UserDto;
import com.example.Utown.model.User;
import org.mapstruct.Mapper;

import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

   UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
   UserDto toDto(User user);
   User toUser(UserDto userDto);
}
