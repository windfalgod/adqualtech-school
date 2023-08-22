package com.management.adqualtechschool.service.impl;

import com.management.adqualtechschool.dto.SubjectDTO;
import com.management.adqualtechschool.entity.Subject;
import com.management.adqualtechschool.entity.TeachSubject;
import com.management.adqualtechschool.repository.SubjectRepository;
import com.management.adqualtechschool.repository.TeachSubjectRepository;
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

    @Autowired
    private TeachSubjectRepository teachSubjectRepository;

    @Override
    public List<SubjectDTO> getAllSubject() {
        List<Subject> subjectList = subjectRepository.findAll();
        return subjectList.stream()
                .map(subject -> modelMapper.map(subject, SubjectDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public SubjectDTO getSubjectByName(String name) {
        Subject subject = subjectRepository.findByName(name);
        return modelMapper.map(subject, SubjectDTO.class);
    }

    @Override
    public List<SubjectDTO> getAllSubjectNotTeachByTeacher(String username) {
        List<Subject> subjectList = subjectRepository.findAll();
        List<TeachSubject> teachSubjects = teachSubjectRepository.findAllByTeacherUsername(username);
        for (int subjectIndex = 0; subjectIndex < subjectList.size(); subjectIndex++) {
            if (!teachSubjects.isEmpty()) {
                for (TeachSubject teachSubject : teachSubjects) {
                    if (teachSubject.getSubject().getId().equals(subjectList.get(subjectIndex).getId())) {
                        subjectList.remove(subjectIndex);
                        if (subjectIndex > 0) {
                            subjectIndex--;
                        } else {
                            subjectIndex = 0;
                        }
                    }
                }
            }
        }
        return subjectList.stream()
                .map(subject -> modelMapper.map(subject, SubjectDTO.class))
                .collect(Collectors.toList());
    }
}
