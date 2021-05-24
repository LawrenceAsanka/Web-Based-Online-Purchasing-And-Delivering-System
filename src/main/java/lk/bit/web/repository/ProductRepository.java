package lk.bit.web.repository;

import lk.bit.web.entity.CustomEntity2;
import lk.bit.web.entity.CustomEntity4;
import lk.bit.web.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {

    @Query(value = "SELECT i.id from item i ORDER BY id DESC LIMIT 1", nativeQuery = true)
    String getLastProductId();
//    Product findFirstLastProductIdByOrderByProductIdDesc() throws Exception;

    @Query(value = "SELECT p.productId AS productId,p.productName AS productName,c.categoryName AS productCategory, " +
            "p.quantitySellingPrice AS productSellingPrice,p.quantityBuyingPrice AS productBuyingPrice,p.currentQuantity AS productQuantity, " +
            "p.discountPerUnit AS discountPerUnit,p.offerStatus AS offerStatus,p.status AS productStatus,p.imageOne AS imageOne," +
            "p.imageTwo AS imageTwo,p.imageThree AS imageThree FROM Product p INNER JOIN p.category c ORDER BY p.productId")
    List<CustomEntity2> getAllProducts();

    @Query(value = "SELECT p.productId AS productId,p.productName AS productName,c.categoryName AS productCategory, " +
            "p.quantitySellingPrice AS productSellingPrice,p.quantityBuyingPrice AS productBuyingPrice,p.currentQuantity AS productQuantity, " +
            "p.discountPerUnit AS discountPerUnit,p.offerStatus AS offerStatus,p.status AS productStatus,p.imageOne AS imageOne, " +
            "p.imageTwo AS imageTwo,p.imageThree AS imageThree FROM Product p INNER JOIN p.category c WHERE c.categoryName = ?1 ORDER BY p.productId")
    List<CustomEntity2> getGroupedProductDetails(String categoryName);

    @Query(value = "SELECT * FROM item WHERE item.status = 'ACTIVE' AND item.current_quantity >= 10 AND item.category_id = ?1 ORDER BY item.id DESC LIMIT 3",
            nativeQuery = true)
    List<Product> getLastActiveThreeProducts(String categoryId);

    @Query(value = "SELECT p FROM Product p WHERE p.category.categoryName = ?1 AND p.status = 'ACTIVE' AND p.currentQuantity >= 10 ORDER BY p.productId")
    List<Product> getProductsByCategory(String categoryName);

    @Query(value = "SELECT p FROM Product p WHERE p.offerStatus=?1 AND p.status=?2 ORDER BY p.productId")
    List<Product> getProductsByOfferStatusEqualsAndStatusEquals(int offerStatus, String productStatus);

    @Query(value = "SELECT p FROM Product p WHERE CONCAT(p.productName, ' ', p.productDescription, ' ', p.category.categoryName, ' ', p.subCategory.subCategoryName) LIKE %?1% " +
            "AND p.status = 'ACTIVE' AND p.currentQuantity >= 10 ORDER BY p.productId")
    List<Product> getProductBySearch(String keyword);

    @Query(value = "SELECT p FROM Product p WHERE p.quantitySellingPrice >= ?1 AND p.quantitySellingPrice <= ?2 AND " +
            "p.status = 'ACTIVE' AND p.currentQuantity >= 10 ORDER BY p.productId")
    List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    @Query(value = "SELECT p FROM Product p WHERE p.subCategory.subCategoryName = ?1 AND p.status = 'ACTIVE' AND p.currentQuantity >= 10 ORDER BY p.productId")
    List<Product> getProductsBySubCategory(String subCategoryName);

    @Query(value = "SELECT p FROM Product p WHERE p.subCategory.subCategoryName = ?1 AND p.weight=?2 AND p.status = 'ACTIVE' AND p.currentQuantity >= 10 ORDER BY p.productId")
    List<Product> getProductsByWeight(String subCategory,String weight);

    @Query(value = "SELECT p FROM Product p WHERE p.subCategory.subCategoryName = ?1 AND p.quantityPerUnit=?2 AND p.status = 'ACTIVE' AND p.currentQuantity >= 10 ORDER BY p.productId")
    List<Product> getProductsByQtyPerUnit(String subCategory,int qpu);

    @Query(value = "SELECT p FROM Product p WHERE p.subCategory.subCategoryName = ?1 AND p.quantitySellingPrice >= ?2 AND p.quantitySellingPrice <= ?3 AND " +
            "p.status = 'ACTIVE' AND p.currentQuantity >= 10 ORDER BY p.productId")
    List<Product> getProductsBySubCategoryWithPriceRange(String subCategory, BigDecimal minPrice, BigDecimal maxPrice);

    @Query(value = "SELECT p FROM Product p WHERE p.category.categoryName = ?1 AND p.weight=?2 AND p.status = 'ACTIVE' AND p.currentQuantity >= 10 ORDER BY p.productId")
    List<Product> getProductsByCategoryWithWeight(String category,String weight);

    @Query(value = "SELECT p FROM Product p WHERE p.category.categoryName = ?1 AND p.quantityPerUnit=?2 AND p.status = 'ACTIVE' AND p.currentQuantity >= 10 ORDER BY p.productId")
    List<Product> getProductsByCategoryWithQtyPerUnit(String category,int qpu);

    @Query(value = "SELECT p FROM Product p WHERE p.category.categoryName = ?1 AND p.quantitySellingPrice >= ?2 AND p.quantitySellingPrice <= ?3 AND " +
            "p.status = 'ACTIVE' AND p.currentQuantity >= 10 ORDER BY p.productId")
    List<Product> getProductsByCategoryWithPriceRange(String category, BigDecimal minPrice, BigDecimal maxPrice);
}
