package com.example.Utown.service;

import com.example.Utown.dto.elementDTO.ElementInfoDto;
import com.example.Utown.dto.optionDTO.OptionInfoDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.model.Dish;
import com.example.Utown.model.Element;
import com.example.Utown.model.Option;
import com.example.Utown.model.UserType.RestaurantAdmin;
import com.example.Utown.repository.OptionRepository;
import com.example.Utown.service.UserTypeService.RestaurantAdminService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OptionServiceImpl implements OptionService {

    private final OptionRepository optionRepository;
    private final ElementService elementService;
    private final RestaurantAdminService restaurantAdminService;

    @Override
    public Option getById(Long id) {
        log.debug("Fetching option by ID: {}", id);
        return optionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Option not found with ID: {}", id);
                    return new ResourceNotFoundException("Option not found", id);
                });
    }

    @Override
    public Set<OptionInfoDto> getOptionsWithElementsByDish(Set<Option> options) {
        Set<Long> optionIds = options.stream()
                .map(Option::getId)
                .collect(Collectors.toSet());

        log.debug("Fetching elements for options with IDs: {}", optionIds);

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
    @Transactional
    @Override
    public void mergeOptions(Dish dish, Set<OptionInfoDto> dtos) {
        Map<Long, Option> existingMap = dish.getOptions().stream()
                .filter(o -> o.getId() != null)
                .collect(Collectors.toMap(Option::getId, o -> o));

        Set<Option> toKeep = new HashSet<>();
        for (OptionInfoDto dto : dtos) {
            Option option;
            if (dto.getId() != null && existingMap.containsKey(dto.getId())) {
                option = existingMap.remove(dto.getId()); // оставляем обновленные, остальное удалим
                option.setName(dto.getName());
                option.setRequired(dto.getRequired());
                option.setMin(dto.getMin());
                option.setMax(dto.getMax());
            } else {
                option = new Option();
                option.setName(dto.getName());
                option.setRequired(dto.getRequired());
                option.setMin(dto.getMin());
                option.setMax(dto.getMax());
                option.setDish(dish); // привязка к Dish
            }

            mergeElements(option, dto.getElements());
            option.setDish(dish);
            toKeep.add(option);
        }

        // Удаляем устаревшие опции
        for (Option obsolete : existingMap.values()) {
            dish.getOptions().remove(obsolete);
        }

        // Добавляем новые и обновленные опции
        dish.getOptions().addAll(toKeep);
    }

    private void mergeElements(Option option, Set<ElementInfoDto> elementDtos) {
        Map<Long, Element> existingMap = option.getElements().stream()
                .filter(e -> e.getId() != null)
                .collect(Collectors.toMap(Element::getId, e -> e));

        Set<Element> toKeep = new HashSet<>();
        for (ElementInfoDto dto : elementDtos) {
            Element element;
            if (dto.getId() != null && existingMap.containsKey(dto.getId())) {
                element = existingMap.remove(dto.getId());
                element.setName(dto.getName());
                element.setDescription(dto.getDescription());
                element.setPrice(dto.getPrice());
            } else {
                element = toElement(dto);
                element.setOption(option); // привязка к Option
            }
            toKeep.add(element);
        }

        // Удаляем устаревшие элементы
        for (Element obsolete : existingMap.values()) {
            option.getElements().remove(obsolete);
        }

        // Добавляем новые/обновленные элементы
        option.getElements().addAll(toKeep);
    }

    private Element toElement(ElementInfoDto dto) {
        Element element = new Element();
        element.setName(dto.getName());
        element.setDescription(dto.getDescription());
        element.setPrice(dto.getPrice());
        return element;
    }
    @Override
    @Transactional
    public Set<Option> createOptionsForDish(Dish dish, Set<OptionInfoDto> optionDtos) {
        if (optionDtos == null || optionDtos.isEmpty()) {
            log.debug("No options to create for dish ID: {}", dish.getId());
            return Collections.emptySet();
        }

        log.info("Creating {} options for dish ID: {}", optionDtos.size(), dish.getId());

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

            Set<Element> elements = elementService.createElementsForOption(option, dto.getElements());
            option.setElements(elements);

            options.add(option);
        }

        return options;
    }

    @Override
    @Transactional
    public void deleteOption(Long id) {
        RestaurantAdmin currentAdmin = restaurantAdminService.getCurrentAdmin();
        Long adminRestaurantId = currentAdmin.getRestaurant().getId();

        Option option = getById(id);
        Long optionRestaurantId = option.getDish().getRestaurant().getId();

        if (!adminRestaurantId.equals(optionRestaurantId)) {
            log.warn("Admin (restaurant ID: {}) attempted to delete unauthorized option ID: {} (belongs to restaurant ID: {})",
                    adminRestaurantId, id, optionRestaurantId);
            throw new AccessDeniedException("You do not have permission to delete this option.");
        }

        log.info("Soft deleting option ID: {} by admin of restaurant ID: {}", id, adminRestaurantId);
        option.setIsActive(false);

        if (option.getElements() != null) {
            for (Element element : option.getElements()) {
                element.setIsActive(false);
            }
        }

        optionRepository.save(option);
    }

}

