package lk.bit.web.business.custom;

import lk.bit.web.dto.product_dto.CategoryDTO;

import java.util.List;

public interface ProductCategoryBO {

    List<CategoryDTO> getAllCategories();

    void saveCategory(CategoryDTO category);

    void updateCategory(CategoryDTO category, String categoryId);

    void deleteCategory(String categoryId);

    boolean existCategory(String categoryId);


}
