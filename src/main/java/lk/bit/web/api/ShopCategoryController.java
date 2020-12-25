package lk.bit.web.api;

import lk.bit.web.business.custom.ShopCategoryBO;
import lk.bit.web.dto.ShopCategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin
@RestController
@RequestMapping("api/v1/shopCategories")
public class ShopCategoryController {

    @Autowired
    private ShopCategoryBO shopCategoryBO;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    private void saveShopCategory(@RequestBody ShopCategoryDTO shopCategoryDetails){
        if (shopCategoryDetails.getCategoryName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        shopCategoryBO.saveShopCategory(shopCategoryDetails.getCategoryName());
    }
}
