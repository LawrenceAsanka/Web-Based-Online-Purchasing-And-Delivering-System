package lk.bit.web.repository;

import lk.bit.web.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {

    Product findFirstLastProductIdByOrderByProductIdDesc();

}
