package lk.bit.web.repository.product_repository;

import lk.bit.web.entity.product_entity.ProductSubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCategoryRepository extends JpaRepository<ProductSubCategory, String> {
}
