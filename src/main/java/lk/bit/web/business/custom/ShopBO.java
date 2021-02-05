package lk.bit.web.business.custom;

import lk.bit.web.entity.Shop;
import lk.bit.web.entity.SuperEntity;

public interface ShopBO extends SuperEntity {

    void saveShop(Shop shop);
}
