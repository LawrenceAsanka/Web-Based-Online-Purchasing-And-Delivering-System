package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.ProductSubCategoryBO;
import lk.bit.web.dto.product_dto.SubCategoryDTO;
import lk.bit.web.entity.ProductSubCategory;
import lk.bit.web.repository.product_repository.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductSubCategoryBOImpl implements ProductSubCategoryBO {

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Override
    public List<SubCategoryDTO> getAllSubCategories() {
        List<ProductSubCategory> allSubCategories = subCategoryRepository.findAll();
        List<SubCategoryDTO> subCategories = new ArrayList<>();
        for (ProductSubCategory subCategory : allSubCategories) {
            subCategories.add(new SubCategoryDTO(subCategory.getSubCategoryId(), subCategory.getSubCategoryName(),
                    subCategory.getStatus(), subCategory.getProductCategory().getCategoryId()));
        }
        return subCategories;
    }

    @Override
    public void saveSubCategory(SubCategoryDTO subCategory) {
        subCategoryRepository.save(new ProductSubCategory(subCategory.getSubCategoryId(),
                subCategory.getSubCategoryName(), subCategory.getStatus(), subCategory.getCategoryId()));
    }

    @Override
    public void updateSubCategory(String name, String status, String categoryId, String subCategoryId) {
        subCategoryRepository.save(new ProductSubCategory(subCategoryId, name, status, categoryId));
    }

    @Override
    public void deleteSubCategory(String subCategoryId) {
        subCategoryRepository.deleteById(subCategoryId);
    }

    @Override
    public boolean subCategoryExist(String subCategoryId) {
        return subCategoryRepository.existsById(subCategoryId);
    }
}
