package JavaProject.MoneyManagement_BE_SE330.transaction;

import JavaProject.MoneyManagement_BE_SE330.transaction.dto.CreateTransactionDTO;
import JavaProject.MoneyManagement_BE_SE330.transaction.dto.TransactionDTO;
import JavaProject.MoneyManagement_BE_SE330.transaction.dto.TransactionDetailDTO;
import JavaProject.MoneyManagement_BE_SE330.transaction.dto.UpdateTransactionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(source = "id", target = "transactionId")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "wallet.id", target = "walletId")
    TransactionDTO toDto(Transaction entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "wallet", ignore = true)
    Transaction toEntity(CreateTransactionDTO dto);

    @Mapping(target = "id", source = "transactionId")
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "wallet", ignore = true)
    void updateEntity(UpdateTransactionDTO dto, @MappingTarget Transaction entity);

    @Mapping(source = "id", target = "transactionId")
    @Mapping(source = "transactionDate", target = "date")
    @Mapping(source = "category.name", target = "category")
    @Mapping(source = "wallet.id", target = "walletId")
    @Mapping(source = "wallet.walletName", target = "walletName")
    @Mapping(target = "time", expression = "java(entity.getTransactionDate().toLocalTime().toString())")
    @Mapping(target = "dayOfWeek", expression = "java(entity.getTransactionDate().getDayOfWeek().toString())")
    @Mapping(target = "month", expression = "java(entity.getTransactionDate().getMonth().toString())")
    TransactionDetailDTO toDetailDto(Transaction entity);
}
