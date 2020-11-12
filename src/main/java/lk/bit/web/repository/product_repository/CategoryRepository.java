package lk.bit.web.repository.product_repository;

import lk.bit.web.entity.product_entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<ProductCategory, String> {
}
