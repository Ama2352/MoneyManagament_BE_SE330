package JavaProject.MoneyManagement_BE_SE330.repositories;

import JavaProject.MoneyManagement_BE_SE330.models.entities.Category;
import JavaProject.MoneyManagement_BE_SE330.models.entities.Budget;
import JavaProject.MoneyManagement_BE_SE330.models.entities.User;
import JavaProject.MoneyManagement_BE_SE330.models.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface BudgetRepository extends JpaRepository<Budget, UUID> {
    List<Budget> findByCategoryAndWalletAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Category category, Wallet wallet, LocalDateTime startDate, LocalDateTime endDate);

    @Modifying
    @Transactional
    @Query("UPDATE Budget ub SET ub.currentSpending = ub.currentSpending + :amount " +
            "WHERE ub.category = :category AND ub.wallet = :wallet " +
            "AND :transactionDate BETWEEN ub.startDate AND ub.endDate")
    void updateCurrentSpending(Category category, Wallet wallet, BigDecimal amount, LocalDateTime transactionDate);

    List<Budget> findByCategoryAndWalletUserAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Category category, User user, LocalDateTime startDate, LocalDateTime endDate);

    List<Budget> findByWalletUser(User user);
}
