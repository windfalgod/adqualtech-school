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

@Data
public class RuleDTO {
    private Long id;

    @NotEmpty(message = Message.RULE_TITLE_NOT_EMPTY)
    @Size(max = 255)
    private String title;

    @NotEmpty(message = Message.RULE_CONTENT_NOT_EMPTY)
    private String content;
    private Account creator;
    private Scope scope;

    @NotNull(message = Message.RULE_START_AT_NOT_NULL)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startAt;

    @NotNull(message = Message.RULE_END_AT_NOT_NULL)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
