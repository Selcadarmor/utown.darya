package com.example.Utown.service;

import com.example.Utown.dto.optionDTO.OptionDto;
import com.example.Utown.dto.optionDTO.OptionInfoDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.OptionMapper;
import com.example.Utown.model.Dish;
import com.example.Utown.model.Element;
import com.example.Utown.model.Option;
import com.example.Utown.repository.DishRepository;
import com.example.Utown.repository.OptionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OptionServiceImpl implements OptionService {

    private final OptionRepository optionRepository;
    private final DishRepository dishRepository;
    private final OptionMapper optionMapper;
    private final ElementService elementService;

    @Override
    public Option create(OptionDto dto) {
        Dish dish = dishRepository.findById(dto.getDish().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found", dto.getDish().getId()));

        Option option = optionMapper.optionDtoToEntity(dto);
        option.setDish(dish);
        // elements оставляем без изменений
        return optionRepository.save(option);
    }

    @Override
    public OptionDto getById(Long id) {
        Option option = optionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Option not found", id));
        return optionMapper.optionToDto(option);
    }

    @Override
    public List<OptionDto> getAll() {
        return optionRepository.findAll().stream()
                .map(optionMapper::optionToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Option update(Long id, OptionDto dto) {
        Option option = optionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Option not found", id));

        Dish dish = dishRepository.findById(dto.getDish().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found", dto.getDish().getId()));

        option.setName(dto.getName());
        option.setRequired(dto.isRequired());
        option.setMin(dto.getMin());
        option.setMax(dto.getMax());
        option.setIsActive(dto.getIsActive());
        option.setDish(dish);

        return optionRepository.save(option);
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
    public void delete(Long id) {
        Option option = optionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Option not found", id));
        optionRepository.delete(option);
    }

    @Override
    @Transactional
    public Set<Option> createOptionsForDish(Dish dish, Set<OptionInfoDto> optionDtos) {
        if (optionDtos == null || optionDtos.isEmpty()) return Collections.emptySet();

        Set<Option> options = new HashSet<>();
        for (OptionInfoDto dto : optionDtos) {
            Option option = Option.builder()
                    .name(dto.getName())
                    .dish(dish)
                    .build();

            // создаём элементы через сервис
            Set<Element> elements = elementService.createElementsForOption(option, dto.getElements());
            option.setElements(elements);

            options.add(option);
        }
        return options;
    }
}

