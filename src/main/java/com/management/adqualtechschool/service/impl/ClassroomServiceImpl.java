package com.management.adqualtechschool.service.impl;

import com.management.adqualtechschool.dto.ClassRoomDTO;
import com.management.adqualtechschool.entity.Classroom;
import com.management.adqualtechschool.repository.ClassroomRepository;
import com.management.adqualtechschool.service.ClassroomService;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassroomServiceImpl implements ClassroomService {

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<ClassRoomDTO> getAllClassroom() {
        List<Classroom> classroomList = classroomRepository.findAll();
        return classroomList.stream()
                .map(classroom -> modelMapper.map(classroom, ClassRoomDTO.class))
                .collect(Collectors.toList());
    }
}
