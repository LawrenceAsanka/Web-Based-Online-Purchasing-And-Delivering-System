package lk.bit.web.api;

import lk.bit.web.business.custom.ProductBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "api/v1/products")
public class ProductController {

    @Autowired
    private ProductBO productBO;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/ids")
    private String getNewProductId(){
        try {
            System.out.println(productBO.getNewProductId());
            return productBO.getNewProductId();
        } catch (Exception e) {
            e.printStackTrace();
            return "P001";
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void saveProduct(@RequestPart("images") List<MultipartFile> imageFiles,
                            @RequestParam("data") String productDetails) {
        if (imageFiles.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        productBO.saveProduct(imageFiles,productDetails);

    }
}
