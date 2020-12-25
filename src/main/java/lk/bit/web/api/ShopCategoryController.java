package lk.bit.web.api;

import lk.bit.web.business.custom.ShopCategoryBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin
@RestController
@RequestMapping("api/v1/shopCategories")
public class ShopCategoryController {

    @Autowired
    private ShopCategoryBO shopCategoryBO;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    private void saveShopCategory(String categoryName){
        if (categoryName == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        shopCategoryBO.saveShopCategory(categoryName);
    }
}
