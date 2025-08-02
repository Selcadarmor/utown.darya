package com.example.Utown.service;

import com.example.Utown.dto.elementDTO.ElementInfoDto;
import com.example.Utown.dto.optionDTO.OptionInfoDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.model.Dish;
import com.example.Utown.model.Element;
import com.example.Utown.model.Option;
import com.example.Utown.model.UserType.RestaurantAdmin;
import com.example.Utown.repository.OptionRepository;
import com.example.Utown.service.UserTypeService.RestaurantAdminServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OptionServiceImpl implements OptionService {

    private final OptionRepository optionRepository;
    private final ElementService elementService;
    private final RestaurantAdminServiceImpl restaurantAdminService;

    @Override
    public Option getById(Long id) {
        Option option = optionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Option not found", id));
        return option;
    }

    @Override
    public Set<OptionInfoDto> getOptionsWithElementsByDish(Set<Option> options) {
        Set<Long> optionIds = options.stream()
                .map(Option::getId)
                .collect(Collectors.toSet());

        Set<Option> optionsWithElements = optionRepository.findAllWithElementsByIds(optionIds);

        Map<Long, Set<Element>> elementsMap = optionsWithElements.stream()
                .collect(Collectors.toMap(Option::getId, Option::getElements));

        return options.stream().map(option -> {
            Set<ElementInfoDto> elementDtos = elementsMap.getOrDefault(option.getId(), Set.of())
                    .stream()
                    .map(element -> new ElementInfoDto(
                            element.getId(),
                            element.getName(),
                            element.getDescription(),
                            element.getPrice()
                    ))
                    .collect(Collectors.toSet());

            return new OptionInfoDto(
                    option.getId(),
                    option.getName(),
                    option.getRequired(),
                    option.getMin(),
                    option.getMax(),
                    elementDtos
            );
        }).collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public Set<Option> updateOptionsForDish(Dish dish, Set<OptionInfoDto> optionDtos) {
        Map<Long, Option> existingOptions = dish.getOptions().stream()
                .collect(Collectors.toMap(Option::getId, Function.identity()));

        Set<Option> updatedOptions = new HashSet<>();

        for (OptionInfoDto optionDto : optionDtos) {
            Option option = existingOptions.containsKey(optionDto.getId())
                    ? existingOptions.remove(optionDto.getId())
                    : new Option();

            option.setName(optionDto.getName());
            option.setRequired(optionDto.getRequired());
            option.setMin(optionDto.getMin());
            option.setMax(optionDto.getMax());
            option.setIsActive(true);
            option.setDish(dish);

            Set<Element> updatedElements = elementService.updateElementsForOption(option, optionDto.getElements());
            option.setElements(updatedElements);

            updatedOptions.add(option);
        }
        dish.getOptions().clear();
        dish.getOptions().addAll(updatedOptions);

        return updatedOptions;
    }

    @Override
    @Transactional
    public Set<Option> createOptionsForDish(Dish dish, Set<OptionInfoDto> optionDtos) {
        if (optionDtos == null || optionDtos.isEmpty()) return Collections.emptySet();

        Set<Option> options = new HashSet<>();
        for (OptionInfoDto dto : optionDtos) {
            Option option = Option.builder()
                    .name(dto.getName())
                    .max(dto.getMax())
                    .min(dto.getMin())
                    .required(dto.getRequired())
                    .isActive(true)
                    .dish(dish)
                    .build();

            // создаём элементы через сервис
            Set<Element> elements = elementService.createElementsForOption(option, dto.getElements());
            option.setElements(elements);

            options.add(option);
        }
        return options;
    }

    @Override
    public void deleteOption(Long id) {
        RestaurantAdmin currentAdmin = restaurantAdminService.getCurrentAdmin();
        Long adminRestaurantId = currentAdmin.getRestaurant().getId();

        Option option = optionRepository.findWithContext(id)
                .orElseThrow(() -> new ResourceNotFoundException("Option", id));

        Long optionRestaurantId = option.getDish().getRestaurant().getId();

        if (!adminRestaurantId.equals(optionRestaurantId)) {
            throw new AccessDeniedException("You do not have permission to delete this option.");
        }
        option.setIsActive(false);

        if(option.getElements() != null) {
            for(Element element : option.getElements()) {
                element.setIsActive(false);
            }
        }
        optionRepository.save(option);
    }
}

