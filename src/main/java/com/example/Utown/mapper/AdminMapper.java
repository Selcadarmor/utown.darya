package com.example.Utown.mapper;

import com.example.Utown.dto.adminDto.AdminCreateRequestDto;
import com.example.Utown.dto.adminDto.AdminDto;
import com.example.Utown.dto.adminDto.AdminUpdateRequestDto;
import com.example.Utown.model.UserType.Admin;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        uses = {
                RoleMapper.class,
                AddressMapper.class,
                RestaurantMapper.class
        }
)
public interface AdminMapper {

    AdminDto toDto(Admin admin);
    Admin toAdmin(AdminCreateRequestDto dto);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAdmin(AdminUpdateRequestDto dto, @MappingTarget Admin admin);
}
