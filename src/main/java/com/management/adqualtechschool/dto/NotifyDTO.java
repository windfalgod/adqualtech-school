package com.management.adqualtechschool.dto;

import com.management.adqualtechschool.common.Message;
import com.management.adqualtechschool.entity.Account;
import com.management.adqualtechschool.entity.Scope;
import java.time.LocalDateTime;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class NotifyDTO {
    private Long id;

    @NotEmpty(message = Message.NOTIFY_TITLE_NOT_EMPTY)
    @Size(max = 255)
    private String title;

    @NotEmpty(message = Message.NOTIFY_CONTENT_NOT_EMPTY)
    private String content;
    private Account creator;
    private Scope scope;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
