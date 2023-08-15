    package com.management.adqualtechschool.dto;

    import com.management.adqualtechschool.common.Message;
    import com.management.adqualtechschool.entity.Classroom;
    import com.management.adqualtechschool.entity.Role;
    import java.time.LocalDate;
    import java.util.Set;
    import javax.validation.constraints.NotEmpty;
    import javax.validation.constraints.NotNull;
    import javax.validation.constraints.Pattern;
    import javax.validation.constraints.Size;
    import lombok.Data;
    import org.springframework.format.annotation.DateTimeFormat;

    import static com.management.adqualtechschool.common.Message.MAX_FIRST_NAME;
    import static com.management.adqualtechschool.common.Message.MAX_LAST_NAME;
    import static com.management.adqualtechschool.common.Message.PATTERN_NAME;

    @Data
    public class AccountCreationDTO {
        private Long id;
        private String username;
        private String password;

        @NotEmpty(message = Message.FIRST_NAME_NOT_NULL)
        @Pattern(regexp = "^[a-zA-Z]{0,30}$", message = PATTERN_NAME)
        @Size(max = 15, message = MAX_FIRST_NAME)

        private String firstName;
        @Pattern(regexp = "^[a-zA-Z]{0,50}$", message = PATTERN_NAME)
        @Size(max = 15, message = MAX_LAST_NAME)
        private String lastName;
        private Boolean gender;

        @NotNull(message = Message.BIRTHDAY_NOT_NULL)
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate birthday;
        private String image;
        private Classroom classRoom;
        private String address;
        private String phone;
        private String email;
        private String level;
        private Long classInChargedId;
        private Set<Role> roles;
    }
