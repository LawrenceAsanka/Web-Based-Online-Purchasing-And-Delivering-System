package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.ProductCategoryBO;
import lk.bit.web.dto.ProductCategoryDTO;
import lk.bit.web.entity.ProductCategory;
import lk.bit.web.repository.product.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoryBoImpl implements ProductCategoryBO {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<ProductCategoryDTO> getAllCategories() {
        List<ProductCategory> allCategories = categoryRepository.findAll();
        List<ProductCategoryDTO> categories = new ArrayList<>();
        for (ProductCategory category : allCategories) {
            categories.add(new ProductCategoryDTO(category.getCategoryId(), category.getCategoryName(),
                    category.getStatus()));
        }
        return categories;
    }

    @Override
    public void saveCategory(ProductCategoryDTO category) {
        categoryRepository.save(new ProductCategory(category.getCategoryId(), category.getCategoryName(),
                category.getStatus()));
    }

    @Override
    public void updateCategory(ProductCategoryDTO category, String categoryId) {
        categoryRepository.save(new ProductCategory(categoryId, category.getCategoryName(),
                category.getStatus()));
    }

    @Override
    public void deleteCategory(String categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public boolean existCategory(String categoryId) {
        return categoryRepository.existsById(categoryId);
    }
}
