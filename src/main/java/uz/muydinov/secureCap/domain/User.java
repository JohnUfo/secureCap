package uz.muydinov.secureCap.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
public class User {
    @Setter
    @Getter
    private Long id;
    
    @NotEmpty(message = "First name cannot be empty")
    private String firstName;
    
    @NotEmpty(message = "Last name cannot be empty")
    private String lastName;

    @Email(message = "Invalid email. Please enter valid email address")
    private String email;
    
    @NotEmpty(message = "Password cannot be empty")
    private String password;
    
    private String address;
    private String phone;
    private String title;
    private String bio;
    private String imageUrl;
    private boolean enabled;
    private boolean isNonLocked;
    private boolean isUsingMfa;
    private LocalDateTime createdAt;
}
