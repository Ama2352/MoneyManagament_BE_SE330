package JavaProject.MoneyManagement_BE_SE330.wallet;

import JavaProject.MoneyManagement_BE_SE330.user.ApplicationUser;
import JavaProject.MoneyManagement_BE_SE330.transaction.Transaction;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "wallets", indexes = {
        @Index(name = "IX_Wallet_WalletName", columnList = "wallet_name"),
        @Index(name = "IX_Wallet_UserId", columnList = "user_id")
})
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "wallet_name", nullable = false)
    private String walletName;

    @Column(nullable = false, precision = 20, scale = 4)
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;

    @OneToMany(mappedBy = "wallet", fetch = FetchType.LAZY)
    private List<Transaction> transactions;
}