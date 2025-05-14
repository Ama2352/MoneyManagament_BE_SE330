package JavaProject.MoneyManagement_BE_SE330.token;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenDTO {

    @NotBlank
    private String expiredToken;

}
