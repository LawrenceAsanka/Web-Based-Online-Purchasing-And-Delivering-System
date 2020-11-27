package lk.bit.web.repository;

import lk.bit.web.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {

    Product findFirstLastProductIdByOrderByProductIdDesc() throws Exception;

    @Query(value = "SELECT p.productId,p.productName,p.category.categoryName, " +
            "p.quantitySellingPrice,p.quantityBuyingPrice,p.status FROM Product p")
    List<Product> getAllProducts();

}
