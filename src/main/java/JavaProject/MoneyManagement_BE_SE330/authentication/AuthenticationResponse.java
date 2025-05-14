package JavaProject.MoneyManagement_BE_SE330.authentication;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private boolean success;
    private String message;
    private String token;
    private List<String> errors;
}
