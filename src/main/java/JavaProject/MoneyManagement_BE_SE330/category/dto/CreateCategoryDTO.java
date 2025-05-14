package JavaProject.MoneyManagement_BE_SE330.category.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryDTO {
    @NotEmpty
    private String name;
}
