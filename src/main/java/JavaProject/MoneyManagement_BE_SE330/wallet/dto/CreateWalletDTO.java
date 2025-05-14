package JavaProject.MoneyManagement_BE_SE330.wallet.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateWalletDTO {
    @NotEmpty
    private String walletName;
    @NotNull
    private BigDecimal balance;
}
