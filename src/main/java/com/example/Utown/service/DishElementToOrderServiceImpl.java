package com.example.Utown.service;

import com.example.Utown.dto.dishElementToOrderDTO.DishElementToOrderDto;
import com.example.Utown.exception.ResourceNotFoundException;
import com.example.Utown.mapper.DishElementToOrderMapper;
import com.example.Utown.model.DishElementToOrder;
import com.example.Utown.model.DishToOrder;
import com.example.Utown.model.Element;
import com.example.Utown.repository.DishElementToOrderRepository;
import com.example.Utown.repository.DishToOrderRepository;
import com.example.Utown.repository.ElementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DishElementToOrderServiceImpl implements DishElementToOrderService {

    private final DishElementToOrderRepository repository;
    private final DishToOrderRepository dishToOrderRepository;
    private final ElementRepository elementRepository;
    private final DishElementToOrderMapper mapper;

    @Override
    public DishElementToOrder create(DishElementToOrderDto dto) {
        DishToOrder dish = dishToOrderRepository.findById(dto.getDishToOrder().getId())
                .orElseThrow(() -> new ResourceNotFoundException("DishToOrder not found", dto.getDishToOrder().getId()));
        Element element = elementRepository.findById(dto.getElement().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Element not found", dto.getElement().getId()));

        DishElementToOrder entity = DishElementToOrder.builder()
                .dishToOrder(dish)
                .element(element)
                .build();

        return repository.save(entity);
    }

    @Override
    public DishElementToOrderDto getById(Long id) {
        DishElementToOrder entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DishElementToOrder not found", id));
        return mapper.toDto(entity);
    }

    @Override
    public List<DishElementToOrderDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public DishElementToOrder update(Long id, DishElementToOrderDto dto) {
        DishElementToOrder entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DishElementToOrder not found", id));

        DishToOrder dish = dishToOrderRepository.findById(dto.getDishToOrder().getId())
                .orElseThrow(() -> new ResourceNotFoundException("DishToOrder not found", dto.getDishToOrder().getId()));
        Element element = elementRepository.findById(dto.getElement().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Element not found", dto.getElement().getId()));

        entity.setDishToOrder(dish);
        entity.setElement(element);

        return repository.save(entity);
    }

    @Override
    public void delete(Long id) {
        DishElementToOrder entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DishElementToOrder not found", id));
        repository.delete(entity);
    }
}

