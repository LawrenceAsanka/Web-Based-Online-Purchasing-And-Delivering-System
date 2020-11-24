package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.ProductSubCategoryBO;
import lk.bit.web.dto.SubCategoryDTO;
import lk.bit.web.entity.ProductCategory;
import lk.bit.web.entity.ProductSubCategory;
import lk.bit.web.repository.SubCategoryRepository;
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
                   subCategory.getCategory().getCategoryId()));
        }
        return subCategories;
    }

    @Override
    public String getSubCategory(int id) {
        String categoryName = subCategoryRepository.getCategoryName(id);
        return categoryName;
    }

    @Override
    public List<SubCategoryDTO> getSortedSubCategories(String categoryName) {
        String categoryId = subCategoryRepository.getCategoryId(categoryName);
        List<ProductSubCategory> sortedSubCategories = subCategoryRepository.sortedSubCategories(categoryId);
        //sortedSubCategories.forEach(System.out::println);
        List<SubCategoryDTO> subCategories = new ArrayList<>();
        for (ProductSubCategory subCategory : sortedSubCategories) {
            subCategories.add(new SubCategoryDTO(subCategory.getSubCategoryId(), subCategory.getSubCategoryName(),
                    subCategory.getCategory().getCategoryId()));
        }
        return subCategories;
    }

    @Override
    public void saveSubCategory(String subCategoryName,String categoryName) {
        String categoryId = subCategoryRepository.getCategoryId(categoryName);
        subCategoryRepository.save(new ProductSubCategory(subCategoryName,categoryId));
    }

    @Override
    public void updateSubCategory(SubCategoryDTO subCategory,int subCategoryId) {
        String categoryId = subCategoryRepository.getCategoryId(subCategory.getCategoryId());
        ProductSubCategory productSubCategory = subCategoryRepository.findById(subCategoryId).get();

        productSubCategory.setSubCategoryName(subCategory.getSubCategoryName());
        productSubCategory.setCategory(new ProductCategory(categoryId));

        subCategoryRepository.save(productSubCategory);
    }

    @Override
    public void deleteSubCategory(String subCategoryId) {

    }

    @Override
    public boolean subCategoryExist(int subCategoryId) {
        return subCategoryRepository.existsById(subCategoryId);
    }


 /*
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
*/
}
