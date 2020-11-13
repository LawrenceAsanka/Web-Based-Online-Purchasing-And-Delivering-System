package lk.bit.web.api.product_api;

import lk.bit.web.business.custom.ProductBO;
import lk.bit.web.dto.product_dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    @Autowired
    private ProductBO productBO;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void saveProduct(@RequestBody ProductDTO product) throws IOException {

        productBO.saveProduct(product);

    }
}
