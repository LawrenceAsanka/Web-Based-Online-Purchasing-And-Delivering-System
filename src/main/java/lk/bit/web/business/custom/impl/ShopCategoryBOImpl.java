package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.ShopCategoryBO;
import lk.bit.web.dto.ShopCategoryDTO;
import lk.bit.web.entity.ShopCategory;
import lk.bit.web.repository.ShopCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ShopCategoryBOImpl implements ShopCategoryBO {

    @Autowired
    private ShopCategoryRepository shopCategoryRepository;

    @Override
    public List<ShopCategoryDTO> getAllShopCategories() {
        List<ShopCategoryDTO> shopCategoryDetails = new ArrayList<>();
        List<ShopCategory> allShopCategories = shopCategoryRepository.findAll();
        for (ShopCategory shopCategory : allShopCategories) {
            shopCategoryDetails.add(new ShopCategoryDTO(shopCategory.getCategoryId(), shopCategory.getCategoryName(),
                    shopCategory.getStatus()));
        }
        return shopCategoryDetails;
    }

    @Override
    public ShopCategoryDTO getShopCategory(int categoryId) {
        ShopCategory shopCategory = shopCategoryRepository.findById(categoryId).get();
        return new ShopCategoryDTO(shopCategory.getCategoryId(), shopCategory.getCategoryName(),
                shopCategory.getStatus());
    }

    @Override
    public void saveShopCategory(String categoryName) {
        shopCategoryRepository.save(new ShopCategory(categoryName, "ACTIVE"));
    }

    @Override
    public void updateShopCategory(String categoryName, String status, int categoryId) {
        shopCategoryRepository.save(new ShopCategory(categoryId, categoryName, status));
    }

    @Override
    public boolean existShopCategory(int categoryId) {
        return shopCategoryRepository.existsById(categoryId);
    }
}
