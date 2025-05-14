package JavaProject.MoneyManagement_BE_SE330.category;

import JavaProject.MoneyManagement_BE_SE330.category.dto.CategoryDTO;
import JavaProject.MoneyManagement_BE_SE330.category.dto.CreateCategoryDTO;
import JavaProject.MoneyManagement_BE_SE330.category.dto.UpdateCategoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(source = "id", target = "categoryId")
    CategoryDTO toDto(Category entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    Category toEntity(CreateCategoryDTO dto);

    @Mapping(target = "id", source = "categoryId")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    void updateEntity(UpdateCategoryDTO dto, @MappingTarget Category entity);
}
