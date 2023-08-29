package com.management.adqualtechschool.service.impl;

import com.management.adqualtechschool.dto.ScopeDTO;
import com.management.adqualtechschool.entity.Scope;
import com.management.adqualtechschool.repository.ScopeRepository;
import com.management.adqualtechschool.service.ScopeService;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.management.adqualtechschool.common.Message.NOT_FOUND_SCOPE;

@Service
public class ScopeServiceImpl implements ScopeService {

    @Autowired
    private ScopeRepository scopeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<ScopeDTO> getAllScope() {
        List<Scope> scopeList = scopeRepository.findAll();
        return scopeList.stream()
                .map(scope -> modelMapper.map(scope, ScopeDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ScopeDTO getScopeByTitle(String title) {
        Scope scope = scopeRepository.findScopeByTitle(title);
        if (scope == null) {
            throw new EntityNotFoundException(NOT_FOUND_SCOPE + title);
        }
        return modelMapper.map(scope, ScopeDTO.class);
    }

    @Override
    public List<ScopeDTO> getAllGradeScope() {
        List<Scope> scopeList = scopeRepository.findAllGrade();
        return scopeList.stream()
                .map(scope -> modelMapper.map(scope, ScopeDTO.class))
                .collect(Collectors.toList());
    }
}
