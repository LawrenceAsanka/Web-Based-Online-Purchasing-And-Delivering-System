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
    public CategoryDTO getCategory(String categoryName){
        ProductCategory category = categoryRepository.findByCategoryName(categoryName);
        return new CategoryDTO(category.getCategoryId(),category.getCategoryName(),
                category.getStatus());
    }

    @Override
    public void saveCategory(CategoryDTO category) {

        //ID generating
        String newCategoryId = "";
        ProductCategory lastCategory = categoryRepository.findFirstLastCategoryIdByOrderByCategoryIdDesc();
        if (lastCategory.getCategoryId() == null) {
            newCategoryId = "CA01";
        } else {
            String replaceCategoryId = lastCategory.getCategoryId().replaceAll("CA", "");
            int categoryId = Integer.parseInt(replaceCategoryId) + 1;
            if (categoryId < 10) {
                newCategoryId = "CA0" + categoryId;
            } else {
                newCategoryId = "CA" + categoryId;
            }

        }
        categoryRepository.save(new ProductCategory(newCategoryId, category.getCategoryName(),
                "ACTIVE"));
    }

    @Override
    public void updateCategory(CategoryDTO category, String categoryId) {
        categoryRepository.save(new ProductCategory(categoryId, category.getCategoryName(),
                category.getStatus()));
    }

    @Override
    public void updateCategoryStatus(String status, String categoryId) {
        ProductCategory category = categoryRepository.findById(categoryId).get();
        category.setStatus(status);
        categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(String categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public String existCategoryByName(String categoryName){
        ProductCategory category = categoryRepository.findByCategoryName(categoryName);
        return category.getCategoryName();

    }

    @Override
    public boolean existCategoryById(String categoryId) {
         return categoryRepository.existsById(categoryId);
    }
}
