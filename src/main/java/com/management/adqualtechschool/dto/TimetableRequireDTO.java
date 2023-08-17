package com.management.adqualtechschool.dto;

import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import static com.management.adqualtechschool.common.Message.END_DATE_NOT_NULL;
import static com.management.adqualtechschool.common.Message.REQUIRED_NOT_EMPTY;
import static com.management.adqualtechschool.common.Message.START_DATE_NOT_NULL;
import static com.management.adqualtechschool.common.Message.STUDY_ROOM_SELECT_NOT_EMPTY;
import static com.management.adqualtechschool.common.Message.TEACHER_SELECT_NOT_EMPTY;

@Data
public class TimetableRequireDTO {
    @NotNull(message = START_DATE_NOT_NULL)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = END_DATE_NOT_NULL)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotEmpty(message = TEACHER_SELECT_NOT_EMPTY)
    private List<Long> teacherList;

    @NotEmpty(message = STUDY_ROOM_SELECT_NOT_EMPTY)
    private List<Long> studyRoomList;

    @NotEmpty(message = REQUIRED_NOT_EMPTY)
    private List<String> requiredList;
}
