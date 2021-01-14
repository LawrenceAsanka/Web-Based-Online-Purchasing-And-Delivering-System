package lk.bit.web.business.custom;

import lk.bit.web.business.SuperBO;
import lk.bit.web.dto.ProductDTO;
import lk.bit.web.util.ProductTM;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    List<ProductDTO> getOfferProducts();

}
