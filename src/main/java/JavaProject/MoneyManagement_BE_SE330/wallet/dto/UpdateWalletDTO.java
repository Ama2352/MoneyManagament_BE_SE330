package JavaProject.MoneyManagement_BE_SE330.wallet.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateWalletDTO {
    @NotNull
    private Long walletId;
    @NotEmpty
    private String walletName;
    @NotNull
    private BigDecimal balance;
}
