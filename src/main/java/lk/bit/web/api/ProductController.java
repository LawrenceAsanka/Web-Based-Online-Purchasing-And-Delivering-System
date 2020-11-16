package lk.bit.web.api;

import lk.bit.web.business.custom.ProductBO;
import lk.bit.web.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping(path = "api/v1/products")
public class ProductController {

    @Autowired
    private ProductBO productBO;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void saveProduct(@RequestBody ProductDTO product) throws IOException {
        if (productBO.existProduct(product.getProductId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        productBO.saveProduct(product);
    }
}
