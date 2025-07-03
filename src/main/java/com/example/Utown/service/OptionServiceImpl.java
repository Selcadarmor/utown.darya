package com.example.Utown.service;

import com.example.Utown.dto.optionDTO.OptionDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.OptionMapper;
import com.example.Utown.model.Dish;
import com.example.Utown.model.Option;
import com.example.Utown.repository.DishRepository;
import com.example.Utown.repository.OptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OptionServiceImpl implements OptionService {

    private final OptionRepository optionRepository;
    private final DishRepository dishRepository;
    private final OptionMapper optionMapper;

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
        option.setMultiple(dto.isMultiple());
        option.setMax(dto.getMax());
        option.setIsActive(dto.getIsActive());
        option.setDish(dish);

        return optionRepository.save(option);
    }

    @Override
    public void delete(Long id) {
        Option option = optionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Option not found", id));
        optionRepository.delete(option);
    }
}

