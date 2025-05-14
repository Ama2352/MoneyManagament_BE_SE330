package JavaProject.MoneyManagement_BE_SE330.transaction.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTransactionDTO {
    @NotNull
    private Long transactionId;
    @NotNull
    private Long categoryId;
    @NotNull
    private BigDecimal amount;
    private String description;
    @NotNull
    private LocalDateTime transactionDate;
    @NotNull
    private Long walletId;
    private String type;
}
