package lk.bit.web.business.custom;

import lk.bit.web.business.SuperBO;
import lk.bit.web.dto.ProductDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductBO extends SuperBO {


    List<ProductDTO> getAllProducts();

    ProductDTO getProduct( String productId);

    void saveProduct(List<MultipartFile> imageFiles,String productDetails);

    boolean existProduct(String productId);

    String getNewProductId() throws Exception;

}
