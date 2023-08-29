package com.management.adqualtechschool.service.impl;

import com.management.adqualtechschool.dto.StudyRoomDTO;
import com.management.adqualtechschool.entity.StudyRoom;
import com.management.adqualtechschool.repository.StudyRoomRepository;
import com.management.adqualtechschool.service.StudyRoomService;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudyRoomServiceImpl implements StudyRoomService {

    @Autowired
    private StudyRoomRepository studyRoomRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<StudyRoomDTO> getAllStudyRoom() {
        List<StudyRoom> studyRoomList = studyRoomRepository.findAllStudyRoom();
        return studyRoomList.stream()
                .map(studyRoom -> modelMapper.map(studyRoom, StudyRoomDTO.class))
                .collect(Collectors.toList());
    }
}
