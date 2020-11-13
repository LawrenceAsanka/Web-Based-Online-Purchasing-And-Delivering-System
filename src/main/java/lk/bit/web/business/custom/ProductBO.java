package lk.bit.web.business.custom;

import lk.bit.web.business.SuperBO;
import lk.bit.web.dto.product_dto.ProductDTO;
import lk.bit.web.repository.product_repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public interface ProductBO extends SuperBO {


    void saveProduct(ProductDTO product) throws IOException;

}
