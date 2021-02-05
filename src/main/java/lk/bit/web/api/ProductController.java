package lk.bit.web.api;

import lk.bit.web.business.custom.ProductBO;
import lk.bit.web.dto.ProductDTO;
import lk.bit.web.util.ProductTM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(path = "api/v1/products")
public class ProductController {

    @Autowired
    private ProductBO productBO;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    private List<ProductTM> getAllProductsDetails() {
        return productBO.getAllProductsDetails();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    private List<ProductTM> getGroupedProductsDetails(@RequestParam("sort") String categoryName) {
        if (categoryName == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return productBO.getGroupedProductDetails(categoryName);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    private ProductDTO getRequestedProduct(@PathVariable("id") String productId) {
        if (!productBO.existProduct(productId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return productBO.getProduct(productId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/categories/{categoryName}")
    private List<ProductDTO> getProductsByCategory(@PathVariable("categoryName") String categoryName) {
        if (categoryName == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return productBO.getProductsByCategory(categoryName);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/ids")
    private String getNewProductId() {
        return productBO.getNewProductId();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/category")
    private List<ProductDTO> getActiveLastThreeProducts(@RequestParam("name") String category) {
        if (category == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return productBO.getActiveLastThreeProducts(category);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/offers")
    private List<ProductDTO> getOfferProducts() {
        return productBO.getOfferedProducts();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    private void saveProduct(@RequestPart("images") List<MultipartFile> imageFiles,
                            @RequestParam("data") String productDetails) {
        if (imageFiles.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        productBO.saveProduct(imageFiles, productDetails);

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    private void updateStatus(@PathVariable("id") String productId, @RequestParam String status) {
        if (!productBO.existProduct(productId) || status == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        productBO.updateStatus(status, productId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    private void updateProduct(@RequestPart("images") List<MultipartFile> imageFiles,
                           @RequestParam @Valid @NotEmpty String status,
                           @RequestParam("data") String productDetails,
                           @PathVariable @Valid @Pattern(regexp = "P\\d{3}") String productId) {
        if (!productBO.existProduct(productId) || status == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        productBO.updateProduct(imageFiles, productDetails, status, productId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}/offers")
    private void updateOfferStatus(@PathVariable String id,@RequestParam int status){
        if (id.isEmpty() && !productBO.existProduct(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        productBO.updateOfferStatus(id,status);
    }
}
