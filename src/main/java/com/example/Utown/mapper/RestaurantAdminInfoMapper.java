package com.example.Utown.mapper;

import com.example.Utown.dto.restaurantAdminDTO.RestaurantAdminCreateDto;
import com.example.Utown.dto.restaurantAdminDTO.RestaurantAdminInfoDto;
import com.example.Utown.model.UserType.RestaurantAdmin;
import com.example.Utown.model.enumFiles.Roles;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {RoleMapper.class, NotificationMapper.class})
public abstract class RestaurantAdminInfoMapper {

    @Autowired
    protected RoleMapper roleMapper;

    public RestaurantAdmin toEntity(RestaurantAdminCreateDto dto) {
        RestaurantAdmin admin = new RestaurantAdmin();

        admin.setUsername(dto.getUsername());
        admin.setPassword(dto.getPassword());
        admin.setFullName(dto.getFullName());


        if (dto.getRoles() != null) {
            admin.setRoles(roleMapper.mapRoles(dto.getRoles()));
        }


        return admin;
    }

    public RestaurantAdminCreateDto toDto(RestaurantAdmin admin) {
        if (admin == null) {
            return null;
        }

        RestaurantAdminCreateDto dto = new RestaurantAdminCreateDto();
        dto.setUsername(admin.getUsername());
        dto.setFullName(admin.getFullName());
        dto.setPassword(null);


        if (admin.getRoles() != null) {
            Set<Roles> roleNames = admin.getRoles().stream()
                    .map(role -> role.getName())
                    .collect(Collectors.toSet());
            dto.setRoles(roleNames);
        }

        return dto;
    }
}
