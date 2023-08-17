package com.management.adqualtechschool.service.impl;

import com.management.adqualtechschool.dto.TimetableRequireDTO;
import com.management.adqualtechschool.entity.TimetableRequire;
import com.management.adqualtechschool.repository.TimetableRequireRepository;
import com.management.adqualtechschool.service.TimetableRequireService;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimetableRequireServiceImpl implements TimetableRequireService {

    @Autowired
    private TimetableRequireRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<TimetableRequireDTO> getAllRequire() {
        List<TimetableRequire> requireList = repository.findAll();
        return requireList.stream()
                .map(require -> modelMapper.map(require, TimetableRequireDTO.class))
                .collect(Collectors.toList());
    }
}
