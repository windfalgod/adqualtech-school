package com.management.adqualtechschool.dto;

import com.management.adqualtechschool.common.Message;
import com.management.adqualtechschool.entity.Account;
import com.management.adqualtechschool.entity.Scope;
import com.management.adqualtechschool.entity.Subject;
import java.time.LocalDateTime;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PrepareExamDTO {
    private Long id;

    @NotEmpty(message = Message.EXAM_TITLE_NOT_EMPTY)
    private String title;
    private String content;
    private Account creator;
    private Scope scope;
    private Subject subject;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @NotNull(message = Message.EXAM_CONTENT_NOT_EMPTY)
    private MultipartFile multipartFile;
}
