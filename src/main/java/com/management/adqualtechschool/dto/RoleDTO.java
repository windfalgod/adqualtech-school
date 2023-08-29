package com.management.adqualtechschool.dto;

import com.management.adqualtechschool.entity.Account;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;

@Data
public class RoleDTO {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<Account> accounts;
}
