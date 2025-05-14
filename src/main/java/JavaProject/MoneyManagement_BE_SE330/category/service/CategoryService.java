package JavaProject.MoneyManagement_BE_SE330.category.service;

import JavaProject.MoneyManagement_BE_SE330.category.dto.CategoryDTO;
import JavaProject.MoneyManagement_BE_SE330.category.dto.CreateCategoryDTO;
import JavaProject.MoneyManagement_BE_SE330.category.dto.UpdateCategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAllCategories();
    CategoryDTO getCategoryById(Long id);
    CategoryDTO createCategory(CreateCategoryDTO dto);
    CategoryDTO updateCategory(UpdateCategoryDTO dto);
    void deleteCategory(Long id);
}
