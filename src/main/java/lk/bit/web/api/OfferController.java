package lk.bit.web.api;

import lk.bit.web.business.custom.ProductBO;
import lk.bit.web.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1/offers")
public class OfferController {

    @Autowired
    private ProductBO productBO;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    private List<ProductDTO> getOfferProduct() {
        return productBO.getOfferProducts();
    }
}
