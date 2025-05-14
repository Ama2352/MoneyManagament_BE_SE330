package JavaProject.MoneyManagement_BE_SE330.category;

import JavaProject.MoneyManagement_BE_SE330.category.dto.CategoryDTO;
import JavaProject.MoneyManagement_BE_SE330.category.dto.CreateCategoryDTO;
import JavaProject.MoneyManagement_BE_SE330.category.dto.UpdateCategoryDTO;
import JavaProject.MoneyManagement_BE_SE330.category.service.CategoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@PreAuthorize("isAuthenticated()")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        try {
            List<CategoryDTO> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            logger.error("Error retrieving categories", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        try {
            CategoryDTO category = categoryService.getCategoryById(id);
            return ResponseEntity.ok(category);
        } catch (RuntimeException e) {
            logger.error("Error retrieving category with ID: {}", id, e);
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            logger.error("Error retrieving category with ID: {}", id, e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CreateCategoryDTO dto) {
        try {
            CategoryDTO created = categoryService.createCategory(dto);
            return ResponseEntity.created(new java.net.URI("/api/categories/" + created.getCategoryId())).body(created);
        } catch (Exception e) {
            logger.error("Error creating category", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody UpdateCategoryDTO dto) {
        try {
            CategoryDTO updated = categoryService.updateCategory(dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            logger.error("Error updating category with ID: {}", dto.getCategoryId(), e);
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            logger.error("Error updating category with ID: {}", dto.getCategoryId(), e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.error("Error deleting category with ID: {}", id, e);
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            logger.error("Error deleting category with ID: {}", id, e);
            return ResponseEntity.status(500).build();
        }
    }
}
