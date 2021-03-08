package lk.bit.web.api;

import lk.bit.web.business.custom.ShopCategoryBO;
import lk.bit.web.dto.ShopCategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/v1/shopCategories")
public class ShopCategoryController {

    @Autowired
    private ShopCategoryBO shopCategoryBO;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    private List<ShopCategoryDTO> getAllShopCategoryDetails(){
        return shopCategoryBO.getAllShopCategories();
    }

   /* @ResponseStatus(HttpStatus.OK)
    @GetMapping(name = "/{categoryId}")
    private ShopCategoryDTO getShopCategoryDetail(@PathVariable int categoryId){
        if (!shopCategoryBO.existShopCategory(categoryId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
      return shopCategoryBO.getShopCategory(categoryId);
    }*/

    @GetMapping()
    private List<ShopCategoryDTO> getAllActiveCategories(@RequestParam String status) {
        return shopCategoryBO.getAllCategoryByIsActivated();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    private void saveShopCategory(@RequestBody ShopCategoryDTO shopCategoryDetails) {
        if (shopCategoryDetails.getCategoryName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        shopCategoryBO.saveShopCategory(shopCategoryDetails.getCategoryName());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{categoryId}")
    private void updateShopCategory(@PathVariable int categoryId, @RequestParam("name") String categoryName){
        if (!shopCategoryBO.existShopCategory(categoryId) || categoryName == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        shopCategoryBO.updateShopCategory(categoryName, categoryId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping
    private void updateShopCategory(@RequestParam("id") int categoryId, @RequestParam int status) {
        if (!shopCategoryBO.existShopCategory(categoryId) || status < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
       shopCategoryBO.updateCategoryStatus(categoryId, status);
    }
}

