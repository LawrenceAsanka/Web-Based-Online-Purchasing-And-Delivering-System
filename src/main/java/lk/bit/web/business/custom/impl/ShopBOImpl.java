package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.ShopBO;
import lk.bit.web.dto.ShopDTO;
import lk.bit.web.entity.CustomerUser;
import lk.bit.web.entity.Shop;
import lk.bit.web.entity.ShopCategory;
import lk.bit.web.repository.CustomerUserRepository;
import lk.bit.web.repository.ShopCategoryRepository;
import lk.bit.web.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ShopBOImpl implements ShopBO {
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private CustomerUserRepository customerUserRepository;
    @Autowired
    private ShopCategoryRepository shopCategoryRepository;

    @Override
    public void saveShop(ShopDTO shopDTO) {
        CustomerUser customer = customerUserRepository.getCustomerByCustomerEmail(shopDTO.getCustomerEmail());
        ShopCategory shopCategory = shopCategoryRepository.findById(shopDTO.getCategory()).get();

        shopRepository.save(new Shop(getLastShopId(), shopDTO.getName(),shopDTO.getContact(), shopDTO.getAddress1(),
                shopDTO.getAddress2(), shopDTO.getCity(), shopDTO.getDistrict(), shopCategory, customer));
    }

    @Override
    public List<ShopDTO> getActiveShopsByCustomer(String customerEmail) {
        CustomerUser customer = customerUserRepository.getCustomerByCustomerEmail(customerEmail);
        List<Shop> shopList = shopRepository.getActiveShopDetailsByCustomer(customer.getCustomerId());

        List<ShopDTO> activeShops = new ArrayList<>();
        for (Shop shop : shopList) {
            activeShops.add(new ShopDTO(shop.getShopId(),shop.getShopName(), shop.getContact(), shop.getShopCategory().getCategoryId(),
                    shop.getAddress1(), shop.getAddress2(), shop.getDistrict(), shop.getCity(), shop.getCustomerUser().getCustomerEmail()));
        }

        return activeShops;
    }

    @Override
    public boolean existShopById(String shopId) {
        return shopRepository.existsById(shopId);
    }

    @Override
    public void updateShopStatus(String shopId) {
        Optional<Shop> shop = shopRepository.findById(shopId);
        if (shop.isPresent()) {
            shop.get().setIsActive(0);
        }
        shopRepository.save(shop.get());
    }

    @Override
    public ShopDTO getShopDetails(String shopId) {
        Optional<Shop> shop = shopRepository.findById(shopId);
        ShopDTO shopDTO = null;
        if (shop.isPresent()) {
            shopDTO = new ShopDTO(
                    shop.get().getShopId(),shop.get().getShopName(),shop.get().getContact(),
                    shop.get().getShopCategory().getCategoryId(),shop.get().getAddress1(),
                    shop.get().getAddress2(),shop.get().getDistrict(),
                    shop.get().getCity(),shop.get().getCustomerUser().getCustomerEmail()
            );
        }
        return shopDTO;
    }

    @Override
    public void updateShop(ShopDTO shopDTO, String shopId) {
        CustomerUser customer = customerUserRepository.getCustomerByCustomerEmail(shopDTO.getCustomerEmail());
        ShopCategory shopCategory = shopCategoryRepository.findById(shopDTO.getCategory()).get();

        Optional<Shop> shop = shopRepository.findById(shopId);
        if (shop.isPresent()) {
            shop.get().setShopName(shopDTO.getName());
            shop.get().setAddress1(shopDTO.getAddress1());
            shop.get().setAddress2(shopDTO.getAddress2());
            shop.get().setCity(shopDTO.getCity());
            shop.get().setContact(shopDTO.getContact());
            shop.get().setDistrict(shopDTO.getDistrict());
            shop.get().setShopCategory(shopCategory);
            shop.get().setCustomerUser(customer);
        }
        shopRepository.save(shop.get());
    }

    private String getLastShopId(){
        String lastShopId = shopRepository.getLastShopId();
        String newId = "";
        if (lastShopId == null) {
             newId = "SP01";
        } else {
            String id = lastShopId.replace("SP0", "");
            int idNumber = Integer.parseInt(id) + 1;
            newId = "SP0" + idNumber;
        }
        return newId;
    }
}
