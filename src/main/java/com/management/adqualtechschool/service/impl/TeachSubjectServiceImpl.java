package com.management.adqualtechschool.service.impl;

import com.management.adqualtechschool.dto.TeachSubjectDTO;
import com.management.adqualtechschool.entity.TeachSubject;
import com.management.adqualtechschool.repository.TeachSubjectRepository;
import com.management.adqualtechschool.service.TeachSubjectService;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeachSubjectServiceImpl implements TeachSubjectService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TeachSubjectRepository teachSubjectRepository;

    @Override
    public List<TeachSubjectDTO> getAllTeachSubject() {
        List<TeachSubject> teachSubjectList = teachSubjectRepository.findAll();
        return teachSubjectList.stream()
                .map(teachSubject -> modelMapper.map(teachSubject, TeachSubjectDTO.class))
                .collect(Collectors.toList());
    }
}
