package JavaProject.MoneyManagement_BE_SE330.wallet;

import JavaProject.MoneyManagement_BE_SE330.wallet.dto.CreateWalletDTO;
import JavaProject.MoneyManagement_BE_SE330.wallet.dto.UpdateWalletDTO;
import JavaProject.MoneyManagement_BE_SE330.wallet.dto.WalletDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface WalletMapper {
    @Mapping(source = "id", target = "walletId")
    WalletDTO toDto(Wallet entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    Wallet toEntity(CreateWalletDTO dto);

    @Mapping(target = "id", source = "walletId")
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    void updateEntity(UpdateWalletDTO dto, @MappingTarget Wallet entity);
}
