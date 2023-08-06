package com.management.adqualtechschool.dto;

import com.management.adqualtechschool.entity.Account;
import com.management.adqualtechschool.entity.Scope;
import com.management.adqualtechschool.entity.Subject;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PrepareExamDTO {
    private Long id;
    private String title;
    private String content;
    private Account creator;
    private Scope scope;
    private Subject subject;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private MultipartFile multipartFile;
}
