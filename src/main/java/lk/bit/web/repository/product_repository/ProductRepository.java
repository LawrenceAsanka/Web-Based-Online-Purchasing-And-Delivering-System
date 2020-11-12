package lk.bit.web.repository.product_repository;

import lk.bit.web.entity.product_entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {

}
