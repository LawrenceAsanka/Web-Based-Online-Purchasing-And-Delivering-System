package lk.bit.web.repository;

import lk.bit.web.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<ProductCategory, String> {

    ProductCategory findByCategoryName(String categoryName);

    ProductCategory findFirstLastCategoryIdByOrderByCategoryIdDesc();

    @Query(value = "SELECT c FROM ProductCategory c WHERE c.status = ?1")
    List<ProductCategory> getAllActiveCategories(String status);
}
