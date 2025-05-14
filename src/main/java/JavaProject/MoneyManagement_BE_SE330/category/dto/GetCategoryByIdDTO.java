package JavaProject.MoneyManagement_BE_SE330.category.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCategoryByIdDTO {
    @NotNull
    private Long categoryId;
}

