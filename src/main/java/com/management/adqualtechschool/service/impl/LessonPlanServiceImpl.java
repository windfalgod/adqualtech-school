package com.management.adqualtechschool.service.impl;

import com.management.adqualtechschool.dto.LessonPlanDTO;
import com.management.adqualtechschool.entity.LessonPlan;
import com.management.adqualtechschool.repository.LessonPlanRepository;
import com.management.adqualtechschool.service.LessonPlanService;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LessonPlanServiceImpl implements LessonPlanService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private LessonPlanRepository lessonPlanRepository;

    @Override
    public List<LessonPlanDTO> getAllLessonPlan() {
        List<LessonPlan> lessonPlanList = lessonPlanRepository.findAll();
        return lessonPlanList.stream()
                .map(lessonPlan -> modelMapper.map(lessonPlan, LessonPlanDTO.class))
                .collect(Collectors.toList());
    }
}
