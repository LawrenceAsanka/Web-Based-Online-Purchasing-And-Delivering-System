package lk.bit.web.repository;

import lk.bit.web.entity.ProductSubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCategoryRepository extends JpaRepository<ProductSubCategory, String> {
}
