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
    @GetMapping
    private List<ProductTM> getGroupedProductsDetails(@RequestParam("sort") String categoryName) {
        if (categoryName == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return productBO.getGroupedProductDetails(categoryName);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    private List<ProductTM> getAllProductsDetails() {
        return productBO.getAllProductsDetails();
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

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    private List<ProductDTO> getProductBySearch(@RequestParam("q") String keyword) {
        if (keyword == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return productBO.getProductsBySearch(keyword);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/priceRange")
    private List<ProductDTO> getProductByPriceRange(@RequestParam("min") String minPrice, @RequestParam("max") String maxPrice) {
        if (minPrice == null && maxPrice == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return productBO.getProductsByPriceRange(minPrice, maxPrice);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/subCategories")
    private List<ProductDTO> getProductByPriceRange(@RequestParam("name") String subCategory) {
//        System.out.println(subCategory);
        if (subCategory == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return productBO.getProductBySubCategory(subCategory);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{subCategory}/qtyPerUnit")
    private List<ProductDTO> getProductByQtyPerUnit(@PathVariable String subCategory, @RequestParam int qtyPerUnit) {
        if (subCategory == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return productBO.getProductByQtyPerUnit(subCategory, qtyPerUnit);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{subCategory}/priceRange")
    private List<ProductDTO> getProductByPriceRange(@PathVariable String subCategory, @RequestParam("min") String minPrice,
                                                    @RequestParam("max") String maxPrice) {
        if (subCategory == null && minPrice == null && maxPrice == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return productBO.getProductsBySubCategoryWithPriceRange(subCategory, minPrice, maxPrice);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{subCategory}/{weight}")
    private List<ProductDTO> getProductByWeight(@PathVariable String subCategory, @PathVariable("weight") String productWeight) {
        if (subCategory == null && productWeight == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return productBO.getProductByWeight(subCategory, productWeight);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/categories")
    private List<ProductDTO> getProductByCategoryWithWeight(@RequestParam("name") String categoryName, @RequestParam("weight") String productWeight) {
        if (categoryName == null && productWeight == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return productBO.getProductByCategoryWithWeight(categoryName, productWeight);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/qtyPerUnit")
    private List<ProductDTO> getProductByCategoryWithQtyPerUnit(@RequestParam String category, @RequestParam("qpu") int qtyPerUnit) {
        if (category == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return productBO.getProductByCategoryWithQtyPerUnit(category, qtyPerUnit);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/")
    private List<ProductDTO> getProductByCategoryWithPriceRange(@RequestParam String category, @RequestParam("min") String minPrice,
                                                    @RequestParam("max") String maxPrice) {
        if (category == null && minPrice == null && maxPrice == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return productBO.getProductsByCategoryWithPriceRange(category, minPrice, maxPrice);
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
