package com.management.adqualtechschool.config;

import com.management.adqualtechschool.dto.PrepareExamDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.CREATOR;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.MONTH_AGO;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.SCOPE;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.SUBJECT;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.TODAY;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.WEEK_AGO;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.YEAR_AGO;
import static com.management.adqualtechschool.common.DisplayTypeAndFilterAndPaginationType.YESTERDAY;

public class CustomExamDTOFilterChain {

    List<PrepareExamDTO> examDTOList;

    public CustomExamDTOFilterChain(List<PrepareExamDTO> listValue) {
        this.examDTOList = listValue;
    }

    public CustomExamDTOFilterChain filterByCreatedAt(String createdAt) {
        if (createdAt == null) {
            return new CustomExamDTOFilterChain(examDTOList);
        }
        examDTOList = examDTOList.stream()
                .filter(eventDTO -> {
                    switch (createdAt) {
                        case TODAY: return eventDTO.getCreatedAt()
                                .isAfter(LocalDateTime.now()
                                        .minusHours(LocalDateTime.now().getHour())
                                        .minusMinutes(LocalDateTime.now().getMinute())
                                        .minusSeconds(LocalDateTime.now().getSecond())
                                );
                        case YESTERDAY: return eventDTO.getCreatedAt()
                                .isAfter(LocalDateTime.now().minusDays(1)
                                        .minusHours(LocalDateTime.now().getHour())
                                        .minusMinutes(LocalDateTime.now().getMinute())
                                        .minusSeconds(LocalDateTime.now().getSecond())
                                );
                        case WEEK_AGO: return eventDTO.getCreatedAt()
                                .isAfter(LocalDateTime.now().minusWeeks(1));
                        case MONTH_AGO: return eventDTO.getCreatedAt()
                                .isAfter(LocalDateTime.now().minusMonths(1));
                        case YEAR_AGO:return eventDTO.getCreatedAt()
                                .isAfter(LocalDateTime.now().minusYears(1));
                        default: return true;
                    }
                })
                .collect(Collectors.toList());
        return new CustomExamDTOFilterChain(examDTOList);
    }

    public CustomExamDTOFilterChain filterBySubjectName(String subjectName) {
        if (subjectName == null) {
            return new CustomExamDTOFilterChain(examDTOList);
        }

        if (subjectName.equals(SUBJECT)) {
            return new CustomExamDTOFilterChain(examDTOList);
        }

        examDTOList = examDTOList.stream()
                .filter(prepareExamDTO -> prepareExamDTO.getSubject().getName().equals(subjectName))
                .collect(Collectors.toList());
        return new CustomExamDTOFilterChain(examDTOList);
    }

    public CustomExamDTOFilterChain filterByScopeName(String scopeName) {
        if(scopeName == null) {
            return new CustomExamDTOFilterChain(examDTOList);
        }

        if (scopeName.equals(SCOPE)) {
            return new CustomExamDTOFilterChain(examDTOList);
        }

        examDTOList = examDTOList.stream()
                .filter(eventDTO -> eventDTO.getScope().getTitle().equals(scopeName))
                .collect(Collectors.toList());
        return new CustomExamDTOFilterChain(examDTOList);
    }

    public CustomExamDTOFilterChain filterByCreatorName(String creatorName) {
        if (creatorName == null) {
            return new CustomExamDTOFilterChain(examDTOList);
        }
        if (creatorName.equals(CREATOR)) {
            return new CustomExamDTOFilterChain(examDTOList);
        }
        examDTOList = examDTOList.stream()
                .filter(eventDTO -> (eventDTO.getCreator().getLastName()
                        + " "
                        + eventDTO.getCreator().getFirstName())
                        .equals(creatorName))
                .collect(Collectors.toList());
        return new CustomExamDTOFilterChain(examDTOList);
    }

    public List<PrepareExamDTO> getExamDTOList() {
        return examDTOList;
    }
}
