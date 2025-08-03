package com.example.Utown.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", uses = {DishToOrderMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {


}

