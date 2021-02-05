package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.ShopBO;
import lk.bit.web.entity.Shop;
import lk.bit.web.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShopBOImpl implements ShopBO {
    @Autowired
    private ShopRepository shopRepository;

    @Override
    public void saveShop(Shop shop) {

    }
}
