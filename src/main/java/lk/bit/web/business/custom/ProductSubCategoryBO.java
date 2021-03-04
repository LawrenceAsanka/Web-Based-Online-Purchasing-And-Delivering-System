package lk.bit.web.business.custom;

import lk.bit.web.business.SuperBO;
import lk.bit.web.dto.SubCategoryDTO;

import java.util.List;

public interface ProductSubCategoryBO extends SuperBO {

    List<SubCategoryDTO> getAllSubCategories();

    String getSubCategory(int id);

    List<SubCategoryDTO> getSortedSubCategories(String categoryName);

    void saveSubCategory(String subCategoryName,String categoryName);

    void updateSubCategory(SubCategoryDTO subCategory,int subCategoryId);

    void deleteSubCategory(String subCategoryId);

    boolean subCategoryExist(int subCategoryId);
}
