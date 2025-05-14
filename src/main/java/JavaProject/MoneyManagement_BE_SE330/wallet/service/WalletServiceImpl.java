package JavaProject.MoneyManagement_BE_SE330.wallet.service;

import JavaProject.MoneyManagement_BE_SE330.user.ApplicationUser;
import JavaProject.MoneyManagement_BE_SE330.user.UserRepository;
import JavaProject.MoneyManagement_BE_SE330.wallet.Wallet;
import JavaProject.MoneyManagement_BE_SE330.wallet.WalletMapper;
import JavaProject.MoneyManagement_BE_SE330.wallet.WalletRepository;
import JavaProject.MoneyManagement_BE_SE330.wallet.dto.CreateWalletDTO;
import JavaProject.MoneyManagement_BE_SE330.wallet.dto.UpdateWalletDTO;
import JavaProject.MoneyManagement_BE_SE330.wallet.dto.WalletDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WalletServiceImpl implements WalletService {

    private static final Logger logger = LoggerFactory.getLogger(WalletServiceImpl.class);

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final WalletMapper walletMapper;

    @Autowired
    public WalletServiceImpl(WalletRepository walletRepository, UserRepository userRepository,
                             WalletMapper walletMapper) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.walletMapper = walletMapper;
    }

    private Long getCurrentUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }

    @Override
    public List<WalletDTO> getAllWallets() {
        logger.info("Fetching all wallets for user");
        Long userId = getCurrentUserId();
        List<Wallet> wallets = walletRepository.findByUserId(userId);
        return wallets.stream().map(walletMapper::toDto).toList();
    }

    @Override
    public WalletDTO getWalletById(Long id) {
        logger.info("Fetching wallet with ID: {}", id);
        Long userId = getCurrentUserId();
        Wallet wallet = walletRepository.findById(id)
                .filter(w -> w.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Wallet not found or access denied"));
        return walletMapper.toDto(wallet);
    }

    @Override
    @Transactional
    public WalletDTO createWallet(CreateWalletDTO dto) {
        logger.info("Creating wallet: {}", dto.getWalletName());
        Long userId = getCurrentUserId();
        ApplicationUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Wallet wallet = walletMapper.toEntity(dto);
        wallet.setUser(user);
        wallet = walletRepository.save(wallet);
        return walletMapper.toDto(wallet);
    }

    @Override
    @Transactional
    public WalletDTO updateWallet(UpdateWalletDTO dto) {
        logger.info("Updating wallet with ID: {}", dto.getWalletId());
        Long userId = getCurrentUserId();
        Wallet wallet = walletRepository.findById(dto.getWalletId())
                .filter(w -> w.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Wallet not found or access denied"));
        walletMapper.updateEntity(dto, wallet);
        wallet = walletRepository.save(wallet);
        return walletMapper.toDto(wallet);
    }

    @Override
    @Transactional
    public void deleteWallet(Long id) {
        logger.info("Deleting wallet with ID: {}", id);
        Long userId = getCurrentUserId();
        Wallet wallet = walletRepository.findById(id)
                .filter(w -> w.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Wallet not found or access denied"));
        walletRepository.delete(wallet);
    }
}

