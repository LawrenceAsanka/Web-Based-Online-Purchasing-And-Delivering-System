package lk.bit.web.business.custom;

import lk.bit.web.dto.ShopDTO;
import lk.bit.web.entity.SuperEntity;

import java.util.List;

public interface ShopBO extends SuperEntity {

    void saveShop(ShopDTO shopDTO);
    List<ShopDTO> getActiveShopsByCustomer(String customerEmail);
    boolean existShopById(String shopId);
    void updateShopStatus(String shopId);
    ShopDTO getShopDetails(String shopId);
    void updateShop(ShopDTO shopDTO,String shopId);
}
