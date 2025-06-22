package com.example.Utown.mapper;

import com.example.Utown.dto.restaurantAdminDto.RestaurantAdminCreateDto;
import com.example.Utown.dto.restaurantAdminDto.RestaurantAdminDto;
import com.example.Utown.dto.restaurantAdminDto.RestaurantAdminUpdateDto;
import com.example.Utown.model.UserType.RestaurantAdmin;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(
        componentModel = "spring",
        uses = {RoleMapper.class,
                AddressMapper.class,
                RestaurantMapper.class
        }
)
public interface RestaurantAdminMapper {
    RestaurantAdminDto toDto(RestaurantAdmin restaurantAdmin);
    RestaurantAdmin toRestaurantAdmin(RestaurantAdminCreateDto dto);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRestaurantAdmin(RestaurantAdminUpdateDto dto, @MappingTarget RestaurantAdmin restaurantAdmin);
}
