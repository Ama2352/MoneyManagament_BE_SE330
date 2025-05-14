package JavaProject.MoneyManagement_BE_SE330.wallet.service;

import JavaProject.MoneyManagement_BE_SE330.wallet.dto.CreateWalletDTO;
import JavaProject.MoneyManagement_BE_SE330.wallet.dto.UpdateWalletDTO;
import JavaProject.MoneyManagement_BE_SE330.wallet.dto.WalletDTO;

import java.util.List;

public interface WalletService {
    List<WalletDTO> getAllWallets();
    WalletDTO getWalletById(Long id);
    WalletDTO createWallet(CreateWalletDTO dto);
    WalletDTO updateWallet(UpdateWalletDTO dto);
    void deleteWallet(Long id);
}
