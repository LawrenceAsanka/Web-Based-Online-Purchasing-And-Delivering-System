package lk.bit.web.business.custom;

import lk.bit.web.business.SuperBO;
import lk.bit.web.dto.ProductDTO;
import lk.bit.web.util.tm.ProductTM;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductBO extends SuperBO {


    List<ProductDTO> getAllProducts();

    ProductDTO getProduct( String productId);

    void saveProduct(List<MultipartFile> imageFiles,String productDetails);

    boolean existProduct(String productId);

    String getNewProductId();

    List<ProductTM> getAllProductsDetails();

    List<ProductTM> getGroupedProductDetails(String categoryName);

    void updateStatus(String status,String productId);

    void updateProduct(List<MultipartFile> imageFiles,String productDetails,String status,String productId);

    List<ProductDTO> getActiveLastThreeProducts(String category);

    List<ProductDTO> getProductsByCategory(String categoryName);

    void updateOfferStatus(String id, int status);

    List<ProductDTO> getOfferedProducts();

    List<ProductDTO> getProductsBySearch(String keyword);

    List<ProductDTO> getProductsByPriceRange(String minPrice, String maxPrice);

    List<ProductDTO> getProductBySubCategory(String subCategoryName);

    List<ProductDTO> getProductByWeight(String subCategoryName, String weight);

    List<ProductDTO> getProductByQtyPerUnit(String subCategoryName, int qpu);

    List<ProductDTO> getProductsBySubCategoryWithPriceRange(String subCategory, String minPrice, String maxPrice);

    List<ProductDTO> getProductByCategoryWithWeight(String categoryName, String weight);

    List<ProductDTO> getProductByCategoryWithQtyPerUnit(String categoryName, int qpu);

    List<ProductDTO> getProductsByCategoryWithPriceRange(String categoryName, String minPrice, String maxPrice);

}
