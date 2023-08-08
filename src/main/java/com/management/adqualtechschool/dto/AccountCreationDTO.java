    package com.management.adqualtechschool.dto;

    import com.management.adqualtechschool.entity.Role;
    import java.time.LocalDateTime;
    import java.util.Set;
    import lombok.Data;

    @Data
    public class AccountCreationDTO {
        private Long id;
        private String username;
        private String password;
        private String firstName;
        private String lastName;
        private Boolean gender;
        private String image;
        private LocalDateTime birthday;
        private Long classId;
        private Set<Role> roles;
    }
