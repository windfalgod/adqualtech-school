package com.management.adqualtechschool.service.impl;

import com.management.adqualtechschool.dto.AccountDTO;
import com.management.adqualtechschool.dto.ClassRoomDTO;
import com.management.adqualtechschool.dto.LessonPlanDTO;
import com.management.adqualtechschool.dto.StudyRoomDTO;
import com.management.adqualtechschool.dto.SubjectDTO;
import com.management.adqualtechschool.dto.SubjectLessonPlanDTO;
import com.management.adqualtechschool.dto.TeachSubjectDTO;
import com.management.adqualtechschool.service.AccountService;
import com.management.adqualtechschool.service.ClassroomService;
import com.management.adqualtechschool.service.LessonPlanService;
import com.management.adqualtechschool.service.StudyRoomService;
import com.management.adqualtechschool.service.SubjectService;
import com.management.adqualtechschool.service.TeachSubjectService;
import com.management.adqualtechschool.service.TimetableService;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimetableServiceImpl implements TimetableService {

    @Autowired
    private StudyRoomService studyRoomService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private LessonPlanService lessonPlanService;

    @Autowired
    private TeachSubjectService teachSubjectService;

    @Autowired
    private AccountService accountService;

    private final static Integer LESSON_PER_WEEK = 30;

    @Override
    public void getParameterForTimetable() {
//        List<StudyRoomDTO> roomDTOList = studyRoomService.getAllStudyRoom();
//        List<ClassRoomDTO> classRoomDTOList = classroomService.getAllClassroom();
//        List<LessonPlanDTO> lessonPlans = lessonPlanService.getAllLessonPlan();
//        List<SubjectDTO> subjectList = subjectService.getAllSubject();
//        List<TeachSubjectDTO> teachSubjectList = teachSubjectService.getAllTeachSubject();
//        List<AccountDTO> teacherList = accountService.getAllTeacherAccount();
//        SubjectLessonPlanDTO[][] subjectLessonPlanDTOs = new SubjectLessonPlanDTO[classRoomDTOList.size()][LESSON_PER_WEEK];
//
//        for (int i = 0; i < classRoomDTOList.size(); i++) {
//            for (int j = 0; j < LESSON_PER_WEEK; j++) {
//                subjectLessonPlanDTOs[i][j] = new SubjectLessonPlanDTO();
//            }
//        }
    }
}
