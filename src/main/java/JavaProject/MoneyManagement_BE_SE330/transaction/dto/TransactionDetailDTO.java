package JavaProject.MoneyManagement_BE_SE330.transaction.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDetailDTO {
    private Long transactionId;
    @NotNull
    private LocalDateTime date;
    @NotEmpty
    private String time;
    @NotEmpty
    private String dayOfWeek;
    @NotEmpty
    private String month;
    @NotNull
    private BigDecimal amount;
    @NotEmpty
    private String type;
    @NotEmpty
    private String category;
    private String description;
    @NotNull
    private Long walletId;
    @NotEmpty
    private String walletName;
}
