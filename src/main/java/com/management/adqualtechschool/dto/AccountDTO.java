package com.management.adqualtechschool.dto;

import com.management.adqualtechschool.common.Message;
import com.management.adqualtechschool.entity.Attendance;
import com.management.adqualtechschool.entity.Classroom;
import com.management.adqualtechschool.entity.Role;
import com.management.adqualtechschool.entity.TeachSubject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import static com.management.adqualtechschool.common.Message.ADDRESS_MAX;
import static com.management.adqualtechschool.common.Message.EMAIL_PATTERN;
import static com.management.adqualtechschool.common.Message.IMAGE_MAX;
import static com.management.adqualtechschool.common.Message.LEVEL_MAX;
import static com.management.adqualtechschool.common.Message.PHONE_PATTERN;
import static com.management.adqualtechschool.common.Message.POSITION_MAX;
import static com.management.adqualtechschool.common.Message.RANK_MAX;

@Data
public class AccountDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private Boolean gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Size(max = 255, message = IMAGE_MAX)
    private String image;
    private Classroom classroom;

    @Size(max = 255, message = ADDRESS_MAX)
    private String address;

    @Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})$|^$", message = PHONE_PATTERN)
    private String phone;

    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}|^$", message = EMAIL_PATTERN)
    private String email;

    @Size(max = 30, message = LEVEL_MAX)
    private String level;

    @Size(max = 50, message = POSITION_MAX)
    private String position;

    @Size(max = 50, message = RANK_MAX)
    private String rank;
    private Set<Role> roles;
    private List<TeachSubject> teachSubjectList;
    private Classroom classInCharged;
    @NotNull(message = Message.EVENT_IMAGE_NOT_EMPTY)
    private MultipartFile multipartFile;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private List<Attendance> attendanceList;
}
