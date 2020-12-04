package lk.bit.web.business.custom;

import lk.bit.web.business.SuperBO;
import lk.bit.web.dto.CategoryDTO;

import java.util.List;

public interface ProductCategoryBO extends SuperBO {

    List<CategoryDTO> getAllCategories();

    CategoryDTO getCategory(String categoryName);

    void saveCategory(CategoryDTO category);

    void updateCategory(CategoryDTO category, String categoryId);

    void updateCategoryStatus(String status,String categoryId);

    void deleteCategory(String categoryId);

    String existCategoryByName(String categoryName);

    boolean existCategoryById(String categoryId);

    List<CategoryDTO> getActiveCategories(String status);
}
