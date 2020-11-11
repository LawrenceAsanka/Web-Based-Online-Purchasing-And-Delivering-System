package lk.bit.web.business.custom;

import lk.bit.web.dto.product_dto.ProductCategoryDTO;

import java.util.List;

public interface ProductCategoryBO {

    List<ProductCategoryDTO> getAllCategories();

    void saveCategory(ProductCategoryDTO category);

    void updateCategory(ProductCategoryDTO category, String categoryId);

    void deleteCategory(String categoryId);

    boolean existCategory(String categoryId);


}
