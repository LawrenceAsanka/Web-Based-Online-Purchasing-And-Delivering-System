package lk.bit.web.business.custom;

import lk.bit.web.business.SuperBO;
import lk.bit.web.dto.SubCategoryDTO;
import lk.bit.web.util.SubCategoryTM;

import java.util.List;

public interface ProductSubCategoryBO extends SuperBO {

    List<SubCategoryDTO> getAllSubCategories();

    void saveSubCategory(String subCategoryName,String categoryName);

    void updateSubCategory(String name,String status,String categoryId,String subCategoryId);

    void deleteSubCategory(String subCategoryId);

    boolean subCategoryExist(String subCategoryId);
}
