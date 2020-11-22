package lk.bit.web.repository;

import lk.bit.web.entity.ProductSubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SubCategoryRepository extends JpaRepository<ProductSubCategory, String> {

    @Query(value = "SELECT c.categoryId FROM ProductCategory c WHERE c.categoryName = ?1")
    String getCategoryId(String categoryName);

    @Query(value = "SELECT c.categoryName FROM ProductSubCategory sc " +
            "INNER JOIN sc.category c WHERE sc.subCategoryId = :subCategoryId")
    String getCategoryName(int subCategoryId);

}
