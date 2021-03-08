package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.ShopCategoryBO;
import lk.bit.web.dto.ShopCategoryDTO;
import lk.bit.web.entity.ShopCategory;
import lk.bit.web.repository.ShopCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

/*    @Override
    public ShopCategoryDTO getShopCategory(int categoryId) {
        ShopCategory shopCategory = shopCategoryRepository.findById(categoryId).get();
        return new ShopCategoryDTO(shopCategory.getCategoryId(), shopCategory.getCategoryName(),
                shopCategory.getStatus());
    }*/

    @Override
    public void saveShopCategory(String categoryName) {
        shopCategoryRepository.save(new ShopCategory(categoryName));
    }

    @Override
    public List<ShopCategoryDTO> getAllCategoryByIsActivated() {
        List<ShopCategory> categories = shopCategoryRepository.getAllCategoriesByIsActive();
        List<ShopCategoryDTO> categoryDTO = new ArrayList<>();
        for (ShopCategory category : categories) {
            categoryDTO.add(new ShopCategoryDTO(category.getCategoryId(), category.getCategoryName(),
                    category.getStatus()));
        }
        return categoryDTO;
    }

    @Override
    public void updateShopCategory(String categoryName, int categoryId) {
        Optional<ShopCategory> optionalShopCategory = shopCategoryRepository.findById(categoryId);

        if (optionalShopCategory.isPresent()) {

            optionalShopCategory.get().setCategoryName(categoryName);

            shopCategoryRepository.save(optionalShopCategory.get());
        }
    }

    @Override
    public boolean existShopCategory(int categoryId) {
        return shopCategoryRepository.existsById(categoryId);
    }

    @Override
    public void updateCategoryStatus(int id, int status) {
        Optional<ShopCategory> optionalShopCategory = shopCategoryRepository.findById(id);

        if (optionalShopCategory.isPresent()) {

            if (status == 1) {
                optionalShopCategory.get().setStatus(0);
            } else {
                optionalShopCategory.get().setStatus(1);
            }

            shopCategoryRepository.save(optionalShopCategory.get());
        }
    }
}
