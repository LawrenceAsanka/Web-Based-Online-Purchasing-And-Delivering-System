package lk.bit.web.business.custom;

import lk.bit.web.business.SuperBO;
import lk.bit.web.dto.ProductDTO;

import java.io.IOException;
import java.util.List;

public interface ProductBO extends SuperBO {


    List<ProductDTO> getAllProducts();

    ProductDTO getProduct( String productId);

    void saveProduct(ProductDTO product) throws IOException;

    boolean existProduct(String productId);

}
