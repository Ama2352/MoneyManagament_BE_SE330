package JavaProject.MoneyManagement_BE_SE330.category.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryDTO {
    @NotNull
    private Long categoryId;
    @NotEmpty
    private String name;
}
