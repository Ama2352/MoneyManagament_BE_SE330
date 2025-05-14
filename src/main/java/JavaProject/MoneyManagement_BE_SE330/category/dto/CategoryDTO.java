package JavaProject.MoneyManagement_BE_SE330.category.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long categoryId;
    @NotEmpty
    private String name;
    @NotNull
    private LocalDateTime createdAt;
}