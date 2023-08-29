package com.management.adqualtechschool.service.impl;

import com.management.adqualtechschool.dto.ClassRoomDTO;
import com.management.adqualtechschool.dto.LessonPlanDTO;
import com.management.adqualtechschool.dto.StudyRoomDTO;
import com.management.adqualtechschool.dto.SubjectDTO;
import com.management.adqualtechschool.dto.SubjectLessonPlanDTO;
import com.management.adqualtechschool.entity.Classroom;
import com.management.adqualtechschool.entity.ElectiveSubject;
import com.management.adqualtechschool.entity.LessonPlan;
import com.management.adqualtechschool.entity.StudyRoom;
import com.management.adqualtechschool.entity.Subject;
import com.management.adqualtechschool.entity.TeachSubject;
import com.management.adqualtechschool.service.AccountService;
import com.management.adqualtechschool.service.ClassroomService;
import com.management.adqualtechschool.service.LessonPlanService;
import com.management.adqualtechschool.service.StudyRoomService;
import com.management.adqualtechschool.service.SubjectService;
import com.management.adqualtechschool.service.TeachSubjectService;
import com.management.adqualtechschool.service.TimetableService;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.ART;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.LITERATURE;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.MATH;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.MUSIC;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.OBLIGATE_COMBINATION;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.PHYSICAL_EDUCATION;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.TECHNOLOGY;

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
    private final static Integer NUM_WEEK_PER_YEAR = 35;
    private final static Integer NUM_LESSON_PER_SESSION = 5;

    // initialize timetable
    private SubjectLessonPlanDTO[][] initializeTimetable(List<ClassRoomDTO> classRoomList) {
        SubjectLessonPlanDTO[][] timetable = new SubjectLessonPlanDTO[classRoomList.size()][LESSON_PER_WEEK];
        for (int classroomNum = 0 ; classroomNum < classRoomList.size() ; classroomNum++) {
            for (int lesson = 0 ; lesson < LESSON_PER_WEEK ; lesson++) {
                timetable[classroomNum][lesson] = new SubjectLessonPlanDTO();
            }
        }
        return timetable;
    }

    @Override
    public void scheduleTimetable() {
        List<StudyRoomDTO> studyRoomList = studyRoomService.getAllStudyRoom();
        List<ClassRoomDTO> classRoomList = classroomService.getAllClassroom();
        List<LessonPlanDTO> lessonPlans = lessonPlanService.getAllLessonPlan();
        SubjectLessonPlanDTO[][] timetable = initializeTimetable(classRoomList);
        int classroomIndex = 0;
        int lessonIndex = 1;
        backTrack(timetable, classRoomList, lessonPlans,
                studyRoomList, classroomIndex, lessonIndex,
                getUnitPerWeekPerSubject());
    }

    // backtracking
     private void backTrack(SubjectLessonPlanDTO[][] timetable, List<ClassRoomDTO> classRoomList,
                            List<LessonPlanDTO> lessonPlans,
                            List<StudyRoomDTO> studyRoomList, int classroomIndex, int lessonIndex,
                            Map<String, Integer> numUnitInWeekPerSubject) {
        if (classroomIndex == classRoomList.size()) {
            return;
        }

         if (lessonIndex == LESSON_PER_WEEK) {
             backTrack(timetable, classRoomList, lessonPlans, studyRoomList,
                     classroomIndex + 1, 1, getUnitPerWeekPerSubject());
             return;
         }

         ClassRoomDTO currentClassroom = classRoomList.get(classroomIndex);
         List<SubjectDTO> listAvailableSubject = availableSubjectListForLesson(timetable, currentClassroom, classroomIndex, lessonIndex);

         for (SubjectDTO subject : listAvailableSubject) {
             if (numUnitInWeekPerSubject.get(subject.getName()) > 0) {
                 assignLesson(timetable, currentClassroom, subject, lessonPlans, studyRoomList, classroomIndex, lessonIndex);
                 numUnitInWeekPerSubject.replace(subject.getName(), numUnitInWeekPerSubject.get(subject.getName()) - 1);
                 backTrack(timetable, classRoomList, lessonPlans, studyRoomList,
                        classroomIndex, lessonIndex + 1, numUnitInWeekPerSubject);
                 timetable[classroomIndex][lessonIndex] = new SubjectLessonPlanDTO(); // backtrack
                 numUnitInWeekPerSubject.replace(subject.getName(), numUnitInWeekPerSubject.get(subject.getName()) + 1);
             }
         }
     }

     private void assignLesson(SubjectLessonPlanDTO[][] timetable, ClassRoomDTO currentClassroom,
                                               SubjectDTO subjectSelect, List<LessonPlanDTO> lessonPlans,
                                               List<StudyRoomDTO> studyRoomList, int classroomIndex, int lessonIndex) {

         TeachSubject teachSubject = subjectSelect.getTeachSubjectList().get(0);
         timetable[classroomIndex][lessonIndex].setLessonPlan(modelMapper.map(lessonPlans.get(lessonIndex), LessonPlan.class));
         timetable[classroomIndex][lessonIndex].setClassroom(modelMapper.map(currentClassroom, Classroom.class));
         timetable[classroomIndex][lessonIndex].setStudyRoom(modelMapper.map(studyRoomList.get(classroomIndex), StudyRoom.class));
         timetable[classroomIndex][lessonIndex].setTeachSubject(teachSubject);
         timetable[classroomIndex][lessonIndex].setCreatedAt(LocalDateTime.now());
         timetable[classroomIndex][lessonIndex].setUpdatedAt(LocalDateTime.now());
     }

    // get number unit of subject in a week
    private Map<String, Integer> getUnitPerWeekPerSubject() {
        List<SubjectDTO> subjectList = subjectService.getAllSubject();
        Map<String, Integer> numUnitPerSubjectPerWeek = new HashMap<>();
        for (SubjectDTO subjectDTO : subjectList) {
            numUnitPerSubjectPerWeek.put(subjectDTO.getName(), subjectDTO.getUnit() / NUM_WEEK_PER_YEAR);
            if (subjectDTO.getName().equals(TECHNOLOGY)
                    || subjectDTO.getName().equals(ART)
                    || subjectDTO.getName().equals(MUSIC)) {
                numUnitPerSubjectPerWeek.put(subjectDTO.getName(), 0);
            }
        }
        return numUnitPerSubjectPerWeek;
    }

    private List<SubjectDTO> availableSubjectListForLesson(SubjectLessonPlanDTO[][] timetable,
                                                           ClassRoomDTO currentClass, int currentClassIndex,
                                                           Integer lessonIndex) {
        int currentLesson = lessonIndex % NUM_LESSON_PER_SESSION;
        int currentDay = lessonIndex / NUM_LESSON_PER_SESSION;
        List<SubjectDTO> subjectList = subjectService.getAllSubject();

        for (int subjectIndex = 0 ; subjectIndex < subjectList.size(); subjectIndex++) {
            SubjectDTO currentSubject = subjectList.get(subjectIndex);

            if (isPhysicalEducationLast(currentSubject, currentLesson)) {
                subjectList.remove(currentSubject);
                subjectIndex = reduceIndex(subjectIndex);

            }
            if (isMathAndLiteratureFirstOrLast(currentSubject, currentLesson)) {
                subjectList.remove(currentSubject);
                subjectIndex = reduceIndex(subjectIndex);

            }
            if (isThreeSameSubjectInOneSession(timetable, currentSubject, currentClassIndex, currentDay, currentLesson)) {
                subjectList.remove(currentSubject);
                subjectIndex = reduceIndex(subjectIndex);
            }

            if (isTwoSameSubjectInOneSession(timetable, currentSubject, currentClassIndex, currentDay, currentLesson)) {
                subjectList.remove(currentSubject);
                subjectIndex = reduceIndex(subjectIndex);
            }
            if (isMathPairingWithLiterature(timetable, currentSubject, currentClassIndex, currentLesson, currentDay)) {
                subjectList.remove(currentSubject);
                subjectIndex = reduceIndex(subjectIndex);
            }
            if (isDifferentCombination(currentClass, currentSubject)) {
                subjectList.remove(currentSubject);
                subjectIndex = reduceIndex(subjectIndex);
            }

            if (isSubjectConflictWithBeforeClass(timetable, currentSubject, currentClassIndex, currentLesson, currentDay)) {
                subjectList.remove(currentSubject);
                subjectIndex = reduceIndex(subjectIndex);
            }
        }
        return subjectList;
    }

    private int reduceIndex(int index) {
        if (index > -1) {
            index--;
        } else {
            index = -1;
        }
        return index;
    }

    // physical at lesson 4 or 5
    private Boolean isPhysicalEducationLast(SubjectDTO currentSubject, int currentLesson) {
        return currentSubject.getName().equals(PHYSICAL_EDUCATION) && currentLesson == 3
                || currentSubject.getName().equals(PHYSICAL_EDUCATION) && currentLesson == 4;
    }

    // math and literature at lesson 1 or 5
    private Boolean isMathAndLiteratureFirstOrLast(SubjectDTO currentSubject, int currentLesson) {
        if (currentSubject.getName().equals(MATH) || currentSubject.getName().equals(LITERATURE)) {
            return currentLesson == 0 || currentLesson == 4;
        }
        return false;
    }

    // check if 3 subject in one session
    private Boolean isThreeSameSubjectInOneSession(SubjectLessonPlanDTO[][] timetable,
                                                   SubjectDTO currentSubject, int currentClassIndex,
                                                   int currentDay, int currentLesson) {
        int countSameSubject = 0;
        int startLessonOfDay = currentDay * NUM_LESSON_PER_SESSION; // start lesson in day follow array
        int currentLessonOfDay = startLessonOfDay + currentLesson; // current lesson in day follow array
        if (currentLesson > 1) {
            if (currentDay == 0) {
                startLessonOfDay++;
            }
            // check if subject same
            for (int checkSubjectIndex = startLessonOfDay; checkSubjectIndex < currentLessonOfDay; checkSubjectIndex++) {
                Subject checkSubject = timetable[currentClassIndex][checkSubjectIndex].getTeachSubject().getSubject();
                if (checkSubject.getName().equals(currentSubject.getName())) {
                    countSameSubject++;
                }
            }
        }
        return countSameSubject == 2;
    }

    private Boolean isTwoSameSubjectInOneSession(SubjectLessonPlanDTO[][] timetable,
                                                   SubjectDTO currentSubject, int currentClassIndex,
                                                   int currentDay, int currentLesson) {
        int countSameSubject = 0;
        // start and current lesson of day is in timetable because start in 0 so minus 1
        int startLessonOfDay = currentDay * NUM_LESSON_PER_SESSION;
        int currentLessonOfDay = startLessonOfDay + currentLesson;
        if (currentLesson % NUM_LESSON_PER_SESSION > 0) {
            if (currentSubject.getUnit() == 70) {
                if (currentDay == 0) {
                    startLessonOfDay++;
                    // if current lesson of day is lesson 2 of monday
                    if (currentLesson == 1) {
                        return false;
                    }
                }
                // check if subject same
                for (int checkSubjectIndex = startLessonOfDay; checkSubjectIndex < currentLessonOfDay; checkSubjectIndex++) {
                    Subject checkSubject = timetable[currentClassIndex][checkSubjectIndex].getTeachSubject().getSubject();
                    if (checkSubject.getName().equals(currentSubject.getName())) {
                        countSameSubject++;
                    }
                }
            }
        }
        return countSameSubject == 1;
    }

    // check math if pairing with literature
    private Boolean isMathPairingWithLiterature(SubjectLessonPlanDTO[][] timetable,
                                                SubjectDTO currentSubject, int currentClassIndex,
                                                int currentLesson, int currentDay) {
        if (currentLesson == 1 && currentDay == 0) {
            return false;
        }
        if (currentLesson > 0) {
            // get index of previous subject in timetable because use array so current lesson is currentLesson - 1 and so on
            int previousSubjectIndex = (currentDay) * NUM_LESSON_PER_SESSION + currentLesson - 1;
            Subject previousSubject = timetable[currentClassIndex][previousSubjectIndex].getTeachSubject().getSubject();

            // if this subject is literature and previous subject is math
            if (currentSubject.getName().equals(MATH)) {
                return previousSubject.getName().equals(LITERATURE);
            }

            // if this subject is math and previous subject is literature
            if (currentSubject.getName().equals(LITERATURE)) {
                return previousSubject.getName().equals(MATH);
            }
        }
        return false;
    }

    private Boolean isDifferentCombination(ClassRoomDTO currentClassroom, SubjectDTO currentSubject) {
        // check if elective subject equal current subject
        for (ElectiveSubject electiveSubject : currentClassroom.getElectiveSubjectList()) {
            if (electiveSubject.getSubject().getName().equals(currentSubject.getName())) {
                return false;
            }
        }
        // check if subject has same combination
        if (currentClassroom.getAcademicTrack().equals(currentSubject.getCombination())
        || currentSubject.getCombination().equals(OBLIGATE_COMBINATION)) {
            return false;
        }
        return true;
    }

    private Boolean isSubjectConflictWithBeforeClass(SubjectLessonPlanDTO[][] timetable,
                                                     SubjectDTO currentSubject, int currentClassIndex,
                                                     int currentLesson, int currentDay) {
        // if current class is start class
        if (currentClassIndex == 0) {
           return false;
        }
        // if current lesson is 1 of monday
        if (currentLesson == 0 && currentDay == 0) {
            return false;
        }
        // traversal class from left to right to validate that no duplicate subject in one time
        for (int startClass = 0; startClass < currentClassIndex; startClass++) {
            int currentLessonOfDay = currentDay * NUM_LESSON_PER_SESSION + currentLesson;
            Subject classBeforeSubject = timetable[startClass][currentLessonOfDay].getTeachSubject().getSubject();
            if (currentSubject.getName().equals(classBeforeSubject.getName())) {
                return true;
            }
        }
        return false;
    }
}
