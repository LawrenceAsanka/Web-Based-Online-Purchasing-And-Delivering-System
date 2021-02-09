package lk.bit.web.api;

import lk.bit.web.business.custom.ShopBO;
import lk.bit.web.dto.ShopDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1/shops")
public class ShopController {
    @Autowired
    private ShopBO shopBO;

    @GetMapping()
    public List<ShopDTO> getActiveShopDetailsByCustomer(@RequestParam String customer){
        if (customer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
       return shopBO.getActiveShopsByCustomer(customer);
    }

    @GetMapping("/{shopId}")
    public ShopDTO getShopDetails(@PathVariable String shopId){
        if (!shopBO.existShopById(shopId) || shopId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return shopBO.getShopDetails(shopId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    private void saveShop(@RequestBody ShopDTO shopDTO){
        shopBO.saveShop(shopDTO);
    }

    @PutMapping()
    public void updateShopStatus(@RequestParam String id){
        if (!shopBO.existShopById(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        shopBO.updateShopStatus(id);
    }

    @PutMapping("/{id}")
    public void updateShop(@RequestBody ShopDTO shopDTO,@PathVariable String id){
        if (!shopBO.existShopById(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        shopBO.updateShop(shopDTO, id);
    }
}
