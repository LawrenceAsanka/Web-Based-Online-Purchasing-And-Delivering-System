package lk.bit.web.repository.product;

import lk.bit.web.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<ProductCategory, String> {
}
