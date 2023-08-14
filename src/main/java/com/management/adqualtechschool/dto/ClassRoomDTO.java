package com.management.adqualtechschool.dto;

import com.management.adqualtechschool.entity.Account;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class ClassRoomDTO {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Account> pupils;
}
