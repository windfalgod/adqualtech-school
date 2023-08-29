package com.management.adqualtechschool.service;

import com.management.adqualtechschool.dto.SubjectDTO;
import java.util.List;

public interface SubjectService {
    List<SubjectDTO> getAllSubject();
    SubjectDTO getSubjectByName(String name);
    List<SubjectDTO> getAllSubjectNotTeachByTeacher(String username);
}
