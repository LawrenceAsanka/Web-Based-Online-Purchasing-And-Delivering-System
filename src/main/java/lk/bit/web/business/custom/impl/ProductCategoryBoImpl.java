package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.ProductCategoryBO;
import lk.bit.web.dto.ProductCategoryDTO;

import java.util.List;

public class ProductCategoryBoImpl implements ProductCategoryBO {
    @Override
    public List<ProductCategoryDTO> getAllCategories() {
        return null;
    }

    @Override
    public void saveCategory(ProductCategoryDTO category) {

    }

    @Override
    public void updateCategory(ProductCategoryDTO category, String categoryId) {

    }

    @Override
    public void deleteCategory(String categoryId) {

    }

    @Override
    public boolean existCategory(String categoryId) {
        return false;
    }
}
