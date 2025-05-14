package JavaProject.MoneyManagement_BE_SE330.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByWalletId(Long walletId);
    List<Transaction> findByWalletUserId(Long userId);

    @Query("SELECT t FROM Transaction t JOIN t.wallet w JOIN t.category c " +
            "WHERE t.transactionDate >= :startDate AND t.transactionDate <= :endDate " +
            "AND w.user.id = :userId " +
            "AND (:type IS NULL OR t.type = :type) " +
            "AND (:category IS NULL OR LOWER(c.name) = LOWER(:category)) " +
            "AND (:timeRange IS NULL OR t.transactionDate BETWEEN :startTime AND :endTime) " +
            "AND (:dayOfWeek IS NULL OR FUNCTION('DAYNAME', t.transactionDate) = :dayOfWeek)")
    List<Transaction> findByDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate,
                                      String type, String category, LocalDateTime startTime, LocalDateTime endTime,
                                      String dayOfWeek);
}
