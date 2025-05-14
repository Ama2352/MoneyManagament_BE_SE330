package JavaProject.MoneyManagement_BE_SE330.transaction;

import JavaProject.MoneyManagement_BE_SE330.transaction.dto.CreateTransactionDTO;
import JavaProject.MoneyManagement_BE_SE330.transaction.dto.TransactionDTO;
import JavaProject.MoneyManagement_BE_SE330.transaction.dto.TransactionDetailDTO;
import JavaProject.MoneyManagement_BE_SE330.transaction.dto.UpdateTransactionDTO;
import JavaProject.MoneyManagement_BE_SE330.transaction.service.TransactionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@PreAuthorize("isAuthenticated()")
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        try {
            List<TransactionDTO> transactions = transactionService.getAllTransactions();
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            logger.error("Error retrieving transactions", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Long id) {
        try {
            TransactionDTO transaction = transactionService.getTransactionById(id);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            logger.error("Error retrieving transaction with ID: {}", id, e);
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            logger.error("Error retrieving transaction with ID: {}", id, e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/wallet/{walletId}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByWalletId(@PathVariable Long walletId) {
        try {
            List<TransactionDTO> transactions = transactionService.getTransactionsByWalletId(walletId);
            return ResponseEntity.ok(transactions);
        } catch (RuntimeException e) {
            logger.error("Error retrieving transactions for wallet ID: {}", walletId, e);
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            logger.error("Error retrieving transactions for wallet ID: {}", walletId, e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@Valid @RequestBody CreateTransactionDTO dto) {
        try {
            TransactionDTO created = transactionService.createTransaction(dto);
            return ResponseEntity.created(new java.net.URI("/api/transactions/" + created.getTransactionId())).body(created);
        } catch (Exception e) {
            logger.error("Error creating transaction", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping
    public ResponseEntity<TransactionDTO> updateTransaction(@Valid @RequestBody UpdateTransactionDTO dto) {
        try {
            TransactionDTO updated = transactionService.updateTransaction(dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            logger.error("Error updating transaction with ID: {}", dto.getTransactionId(), e);
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            logger.error("Error updating transaction with ID: {}", dto.getTransactionId(), e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        try {
            transactionService.deleteTransaction(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.error("Error deleting transaction with ID: {}", id, e);
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            logger.error("Error deleting transaction with ID: {}", id, e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<TransactionDetailDTO>> getTransactionsByDateRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String timeRange,
            @RequestParam(required = false) String dayOfWeek) {
        try {
            if (type != null && !type.equalsIgnoreCase("income") && !type.equalsIgnoreCase("expense")) {
                return ResponseEntity.badRequest().body(null);
            }
            List<TransactionDetailDTO> transactions = transactionService.getTransactionsByDateRange(
                    startDate, endDate, type, category, timeRange, dayOfWeek);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            logger.error("Error retrieving transactions by date range", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<TransactionDetailDTO>> searchTransactions(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String amountRange,
            @RequestParam(required = false) String keywords,
            @RequestParam(required = false) String timeRange,
            @RequestParam(required = false) String dayOfWeek) {
        try {
            List<TransactionDetailDTO> transactions = transactionService.searchTransactions(
                    startDate, endDate, type, category, amountRange, keywords, timeRange, dayOfWeek);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            logger.error("Error searching transactions", e);
            return ResponseEntity.status(500).body(null);
        }
    }
}
