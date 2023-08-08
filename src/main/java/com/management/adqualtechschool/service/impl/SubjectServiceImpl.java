package com.management.adqualtechschool.service.impl;

import com.management.adqualtechschool.dto.SubjectDTO;
import com.management.adqualtechschool.entity.Subject;
import com.management.adqualtechschool.repository.SubjectRepository;
import com.management.adqualtechschool.service.SubjectService;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<SubjectDTO> getAllSubject() {
        List<Subject> subjectList = subjectRepository.findAll();
        return subjectList.stream()
                .map(subject -> modelMapper.map(subject, SubjectDTO.class))
                .collect(Collectors.toList());
    }
}
