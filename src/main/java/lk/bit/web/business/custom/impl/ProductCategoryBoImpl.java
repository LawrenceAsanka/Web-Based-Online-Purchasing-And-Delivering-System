package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.ProductCategoryBO;
import lk.bit.web.dto.CategoryDTO;
import lk.bit.web.entity.ProductCategory;
import lk.bit.web.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductCategoryBoImpl implements ProductCategoryBO {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<ProductCategory> allCategories = categoryRepository.findAll();
        List<CategoryDTO> categories = new ArrayList<>();
        for (ProductCategory category : allCategories) {
            categories.add(new CategoryDTO(category.getCategoryId(), category.getCategoryName(),
                    category.getStatus()));
        }
        return categories;
    }

    @Override
    public void saveCategory(CategoryDTO category) {
        categoryRepository.save(new ProductCategory(category.getCategoryId(), category.getCategoryName(),
                category.getStatus()));
    }

    @Override
    public void updateCategory(CategoryDTO category, String categoryId) {
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
