package JavaProject.MoneyManagement_BE_SE330.account.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDTO {
    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String password;

    @NotEmpty
    private String confirmPassword;
}
