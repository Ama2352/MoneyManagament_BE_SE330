package JavaProject.MoneyManagement_BE_SE330.category;

import JavaProject.MoneyManagement_BE_SE330.user.ApplicationUser;
import JavaProject.MoneyManagement_BE_SE330.transaction.Transaction;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories", indexes = {
        @Index(name = "IX_Category_Name", columnList = "name"),
        @Index(name = "IX_Category_UserId", columnList = "user_id")
})
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Transaction> transactions;
}