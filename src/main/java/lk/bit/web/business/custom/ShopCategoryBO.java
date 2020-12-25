package lk.bit.web.business.custom;

import lk.bit.web.business.SuperBO;
import lk.bit.web.dto.ShopCategoryDTO;

import java.util.List;

public interface ShopCategoryBO extends SuperBO {

    List<ShopCategoryDTO> getAllShopCategories();

    ShopCategoryDTO getShopCategory(int categoryId);

    void saveShopCategory(String categoryName);

    void updateShopCategory(String categoryName,String status,int categoryId);

    boolean existShopCategory(int categoryId);
}
