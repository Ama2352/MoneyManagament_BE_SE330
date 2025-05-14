package JavaProject.MoneyManagement_BE_SE330.transaction;

import JavaProject.MoneyManagement_BE_SE330.category.Category;
import JavaProject.MoneyManagement_BE_SE330.wallet.Wallet;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions", indexes = {
        @Index(name = "IX_Transaction_WalletID", columnList = "wallet_id"),
        @Index(name = "IX_Transaction_CategoryID", columnList = "category_id"),
        @Index(name = "IX_Transaction_TransactionDate", columnList = "transaction_date"),
        @Index(name = "IX_Transaction_Type", columnList = "type")
})
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 20, scale = 4)
    private BigDecimal amount;

    @Column
    private String description;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @Column(nullable = false, length = 20)
    private String type = "expense";

    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}