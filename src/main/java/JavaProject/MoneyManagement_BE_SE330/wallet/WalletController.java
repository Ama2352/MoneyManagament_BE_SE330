package JavaProject.MoneyManagement_BE_SE330.wallet;

import JavaProject.MoneyManagement_BE_SE330.wallet.dto.CreateWalletDTO;
import JavaProject.MoneyManagement_BE_SE330.wallet.dto.UpdateWalletDTO;
import JavaProject.MoneyManagement_BE_SE330.wallet.dto.WalletDTO;
import JavaProject.MoneyManagement_BE_SE330.wallet.service.WalletService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallets")
@PreAuthorize("isAuthenticated()")
public class WalletController {

    private static final Logger logger = LoggerFactory.getLogger(WalletController.class);

    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping
    public ResponseEntity<List<WalletDTO>> getAllWallets() {
        try {
            List<WalletDTO> wallets = walletService.getAllWallets();
            return ResponseEntity.ok(wallets);
        } catch (Exception e) {
            logger.error("Error retrieving wallets", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<WalletDTO> getWalletById(@PathVariable Long id) {
        try {
            WalletDTO wallet = walletService.getWalletById(id);
            return ResponseEntity.ok(wallet);
        } catch (RuntimeException e) {
            logger.error("Error retrieving wallet with ID: {}", id, e);
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            logger.error("Error retrieving wallet with ID: {}", id, e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<WalletDTO> createWallet(@Valid @RequestBody CreateWalletDTO dto) {
        try {
            WalletDTO created = walletService.createWallet(dto);
            return ResponseEntity.created(new java.net.URI("/api/wallets/" + created.getWalletId())).body(created);
        } catch (Exception e) {
            logger.error("Error creating wallet", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping
    public ResponseEntity<WalletDTO> updateWallet(@Valid @RequestBody UpdateWalletDTO dto) {
        try {
            WalletDTO updated = walletService.updateWallet(dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            logger.error("Error updating wallet with ID: {}", dto.getWalletId(), e);
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            logger.error("Error updating wallet with ID: {}", dto.getWalletId(), e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWallet(@PathVariable Long id) {
        try {
            walletService.deleteWallet(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.error("Error deleting wallet with ID: {}", id, e);
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            logger.error("Error deleting wallet with ID: {}", id, e);
            return ResponseEntity.status(500).build();
        }
    }
}
