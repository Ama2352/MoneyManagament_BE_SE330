package JavaProject.MoneyManagement_BE_SE330.category.service;

import JavaProject.MoneyManagement_BE_SE330.category.Category;
import JavaProject.MoneyManagement_BE_SE330.category.CategoryMapper;
import JavaProject.MoneyManagement_BE_SE330.category.CategoryRepository;
import JavaProject.MoneyManagement_BE_SE330.category.dto.CategoryDTO;
import JavaProject.MoneyManagement_BE_SE330.category.dto.CreateCategoryDTO;
import JavaProject.MoneyManagement_BE_SE330.category.dto.UpdateCategoryDTO;
import JavaProject.MoneyManagement_BE_SE330.user.ApplicationUser;
import JavaProject.MoneyManagement_BE_SE330.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, UserRepository userRepository,
                               CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.categoryMapper = categoryMapper;
    }

    private Long getCurrentUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        logger.info("Fetching all categories for user");
        Long userId = getCurrentUserId();
        List<Category> categories = categoryRepository.findByUserId(userId);
        return categories.stream().map(categoryMapper::toDto).toList();
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        logger.info("Fetching category with ID: {}", id);
        Long userId = getCurrentUserId();
        Category category = categoryRepository.findById(id)
                .filter(c -> c.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Category not found or access denied"));
        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional
    public CategoryDTO createCategory(CreateCategoryDTO dto) {
        logger.info("Creating category: {}", dto.getName());
        Long userId = getCurrentUserId();
        ApplicationUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Category category = categoryMapper.toEntity(dto);
        category.setUser(user);
        category = categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(UpdateCategoryDTO dto) {
        logger.info("Updating category with ID: {}", dto.getCategoryId());
        Long userId = getCurrentUserId();
        Category category = categoryRepository.findById(dto.getCategoryId())
                .filter(c -> c.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Category not found or access denied"));
        categoryMapper.updateEntity(dto, category);
        category = categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        logger.info("Deleting category with ID: {}", id);
        Long userId = getCurrentUserId();
        Category category = categoryRepository.findById(id)
                .filter(c -> c.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Category not found or access denied"));
        categoryRepository.delete(category);
    }
}
