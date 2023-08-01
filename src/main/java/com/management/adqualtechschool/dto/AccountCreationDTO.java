    package com.management.adqualtechschool.dto;

    import com.management.adqualtechschool.common.Message;
    import com.management.adqualtechschool.entity.Classroom;
    import com.management.adqualtechschool.entity.Role;
    import java.time.LocalDateTime;
    import java.util.Set;
    import javax.validation.constraints.NotEmpty;
    import javax.validation.constraints.Pattern;
    import lombok.Data;

    @Data
    public class AccountCreationDTO {
        private Long id;
        private String username;

        @NotEmpty(message = Message.PASSWORD_NOT_NULL)
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,32}$",
        message = Message.PASSWORD_REGEXP)
        private String password;
        private String firstName;
        private String lastName;
        private Boolean gender;
        private LocalDateTime birthday;
        private String image;
        private Classroom classRoom;
        private String address;
        private String phone;
        private String email;
        private String level;
        private Long classInChargedId;
        private Set<Role> roles;
    }
