package JavaProject.MoneyManagement_BE_SE330.transaction.service;

import JavaProject.MoneyManagement_BE_SE330.transaction.dto.CreateTransactionDTO;
import JavaProject.MoneyManagement_BE_SE330.transaction.dto.TransactionDTO;
import JavaProject.MoneyManagement_BE_SE330.transaction.dto.TransactionDetailDTO;
import JavaProject.MoneyManagement_BE_SE330.transaction.dto.UpdateTransactionDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    List<TransactionDTO> getAllTransactions();
    TransactionDTO getTransactionById(Long id);
    List<TransactionDTO> getTransactionsByWalletId(Long walletId);
    TransactionDTO createTransaction(CreateTransactionDTO dto);
    TransactionDTO updateTransaction(UpdateTransactionDTO dto);
    void deleteTransaction(Long id);
    List<TransactionDetailDTO> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate,
                                                          String type, String category, String timeRange, String dayOfWeek);
    List<TransactionDetailDTO> searchTransactions(LocalDateTime startDate, LocalDateTime endDate,
                                                  String type, String category, String amountRange, String keywords,
                                                  String timeRange, String dayOfWeek);
}
