package JavaProject.MoneyManagement_BE_SE330.transaction.service;

import JavaProject.MoneyManagement_BE_SE330.category.Category;
import JavaProject.MoneyManagement_BE_SE330.category.CategoryRepository;
import JavaProject.MoneyManagement_BE_SE330.transaction.Transaction;
import JavaProject.MoneyManagement_BE_SE330.transaction.TransactionMapper;
import JavaProject.MoneyManagement_BE_SE330.transaction.TransactionRepository;
import JavaProject.MoneyManagement_BE_SE330.transaction.dto.CreateTransactionDTO;
import JavaProject.MoneyManagement_BE_SE330.transaction.dto.TransactionDTO;
import JavaProject.MoneyManagement_BE_SE330.transaction.dto.TransactionDetailDTO;
import JavaProject.MoneyManagement_BE_SE330.transaction.dto.UpdateTransactionDTO;
import JavaProject.MoneyManagement_BE_SE330.user.UserRepository;
import JavaProject.MoneyManagement_BE_SE330.wallet.Wallet;
import JavaProject.MoneyManagement_BE_SE330.wallet.WalletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final TransactionMapper transactionMapper;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, WalletRepository walletRepository,
                                  CategoryRepository categoryRepository, UserRepository userRepository,
                                  TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.transactionMapper = transactionMapper;
    }

    private Long getCurrentUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }

    @Override
    public List<TransactionDTO> getAllTransactions() {
        logger.info("Fetching all transactions for user");
        Long userId = getCurrentUserId();
        List<Transaction> transactions = transactionRepository.findByWalletUserId(userId);
        return transactions.stream().map(transactionMapper::toDto).toList();
    }

    @Override
    public TransactionDTO getTransactionById(Long id) {
        logger.info("Fetching transaction with ID: {}", id);
        Long userId = getCurrentUserId();
        Transaction transaction = transactionRepository.findById(id)
                .filter(t -> t.getWallet().getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Transaction not found or access denied"));
        return transactionMapper.toDto(transaction);
    }

    @Override
    public List<TransactionDTO> getTransactionsByWalletId(Long walletId) {
        logger.info("Fetching transactions for wallet ID: {}", walletId);
        Long userId = getCurrentUserId();
        walletRepository.findById(walletId)
                .filter(w -> w.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Wallet not found or access denied"));
        List<Transaction> transactions = transactionRepository.findByWalletId(walletId);
        return transactions.stream().map(transactionMapper::toDto).toList();
    }

    @Override
    @Transactional
    public TransactionDTO createTransaction(CreateTransactionDTO dto) {
        logger.info("Creating transaction");
        Long userId = getCurrentUserId();
        Wallet wallet = walletRepository.findById(dto.getWalletId())
                .filter(w -> w.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Wallet not found or access denied"));
        Category category = categoryRepository.findById(dto.getCategoryId())
                .filter(c -> c.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Category not found or access denied"));
        Transaction transaction = transactionMapper.toEntity(dto);
        transaction.setWallet(wallet);
        transaction.setCategory(category);
        if (dto.getType() != null && (dto.getType().equalsIgnoreCase("income") || dto.getType().equalsIgnoreCase("expense"))) {
            transaction.setType(dto.getType().toLowerCase());
        } else {
            transaction.setType(dto.getAmount().compareTo(BigDecimal.ZERO) < 0 ? "expense" : "income");
        }
        transaction = transactionRepository.save(transaction);
        return transactionMapper.toDto(transaction);
    }

    @Override
    @Transactional
    public TransactionDTO updateTransaction(UpdateTransactionDTO dto) {
        logger.info("Updating transaction with ID: {}", dto.getTransactionId());
        Long userId = getCurrentUserId();
        Transaction transaction = transactionRepository.findById(dto.getTransactionId())
                .filter(t -> t.getWallet().getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Transaction not found or access denied"));
        Wallet wallet = walletRepository.findById(dto.getWalletId())
                .filter(w -> w.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Wallet not found or access denied"));
        Category category = categoryRepository.findById(dto.getCategoryId())
                .filter(c -> c.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Category not found or access denied"));
        transactionMapper.updateEntity(dto, transaction);
        transaction.setWallet(wallet);
        transaction.setCategory(category);
        if (dto.getType() != null && (dto.getType().equalsIgnoreCase("income") || dto.getType().equalsIgnoreCase("expense"))) {
            transaction.setType(dto.getType().toLowerCase());
        } else {
            transaction.setType(dto.getAmount().compareTo(BigDecimal.ZERO) < 0 ? "expense" : "income");
        }
        transaction = transactionRepository.save(transaction);
        return transactionMapper.toDto(transaction);
    }

    @Override
    @Transactional
    public void deleteTransaction(Long id) {
        logger.info("Deleting transaction with ID: {}", id);
        Long userId = getCurrentUserId();
        Transaction transaction = transactionRepository.findById(id)
                .filter(t -> t.getWallet().getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Transaction not found or access denied"));
        transactionRepository.delete(transaction);
    }

    @Override
    public List<TransactionDetailDTO> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate,
                                                                 String type, String category, String timeRange, String dayOfWeek) {
        logger.info("Fetching transactions by date range: {} to {}", startDate, endDate);
        Long userId = getCurrentUserId();
        LocalDateTime startTime = null, endTime = null;
        if (timeRange != null && timeRange.contains("-")) {
            String[] times = timeRange.split("-");
            startTime = LocalDateTime.of(startDate.toLocalDate(), LocalTime.parse(times[0]));
            endTime = LocalDateTime.of(endDate.toLocalDate(), LocalTime.parse(times[1]));
        }
        List<Transaction> transactions = transactionRepository.findByDateRange(userId, startDate, endDate,
                type, category, startTime, endTime, dayOfWeek);
        return transactions.stream().map(transactionMapper::toDetailDto).toList();
    }

    @Override
    public List<TransactionDetailDTO> searchTransactions(LocalDateTime startDate, LocalDateTime endDate,
                                                         String type, String category, String amountRange, String keywords,
                                                         String timeRange, String dayOfWeek) {
        logger.info("Searching transactions with filters");
        Long userId = getCurrentUserId();
        LocalDateTime startTime = null, endTime = null;
        if (timeRange != null && timeRange.contains("-")) {
            String[] times = timeRange.split("-");
            startTime = LocalDateTime.of(startDate.toLocalDate(), LocalTime.parse(times[0]));
            endTime = LocalDateTime.of(endDate.toLocalDate(), LocalTime.parse(times[1]));
        }
        List<Transaction> transactions = transactionRepository.findByDateRange(userId, startDate, endDate,
                type, category, startTime, endTime, dayOfWeek);
        if (amountRange != null && amountRange.contains("-")) {
            String[] range = amountRange.split("-");
            BigDecimal minAmount = new BigDecimal(range[0]);
            BigDecimal maxAmount = new BigDecimal(range[1]);
            transactions = transactions.stream()
                    .filter(t -> t.getAmount().abs().compareTo(minAmount) >= 0 && t.getAmount().abs().compareTo(maxAmount) <= 0)
                    .toList();
        }
        if (keywords != null) {
            transactions = transactions.stream()
                    .filter(t -> t.getDescription() != null && t.getDescription().contains(keywords))
                    .toList();
        }
        return transactions.stream().map(transactionMapper::toDetailDto).toList();
    }
}
