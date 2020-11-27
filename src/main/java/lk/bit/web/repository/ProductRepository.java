package lk.bit.web.repository;

import lk.bit.web.entity.CustomEntity2;
import lk.bit.web.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {

    Product findFirstLastProductIdByOrderByProductIdDesc() throws Exception;

    @Query(value = "SELECT p.productId AS productId,p.productName AS productName,c.categoryName AS productCategory, " +
            "p.quantitySellingPrice AS productSellingPrice,p.quantityBuyingPrice AS productBuyingPrice,p.currentQuantity AS productQuantity, " +
            "p.status AS productStatus FROM Product p INNER JOIN p.category c ORDER BY p.productId")
    List<CustomEntity2> getAllProducts();

    @Query(value = "SELECT p.productId AS productId,p.productName AS productName,c.categoryName AS productCategory, " +
            "p.quantitySellingPrice AS productSellingPrice,p.quantityBuyingPrice AS productBuyingPrice,p.currentQuantity AS productQuantity, " +
            "p.status AS productStatus FROM Product p INNER JOIN p.category c WHERE c.categoryName = ?1 ORDER BY p.productId")
    List<CustomEntity2> getGroupedProductDetails(String categoryName);

}
