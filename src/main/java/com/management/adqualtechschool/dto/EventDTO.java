package com.management.adqualtechschool.dto;

import com.management.adqualtechschool.common.Message;
import com.management.adqualtechschool.entity.Account;
import com.management.adqualtechschool.entity.Scope;
import java.time.LocalDateTime;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

@Data
public class EventDTO {
    private Long id;

    @NotEmpty(message = Message.EVENT_TITLE_NOT_EMPTY)
    @Size(max = 255)
    private String title;

    @NotEmpty(message = Message.EVENT_CONTENT_NOT_EMPTY)
    private String content;

    @Size(max = 255)
    private String image;
    private Account creator;
    private Scope scope;

    @NotNull(message = Message.EVENT_START_AT_NOT_NULL)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startAt;

    @NotNull(message = Message.EVENT_END_AT_NOT_NULL)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @NotNull(message = Message.EVENT_IMAGE_NOT_EMPTY)
    private MultipartFile multipartFile;
}
