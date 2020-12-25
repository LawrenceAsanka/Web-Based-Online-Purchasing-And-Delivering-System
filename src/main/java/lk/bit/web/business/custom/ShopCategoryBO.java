package lk.bit.web.business.custom;

import lk.bit.web.business.SuperBO;

public interface ShopCategoryBO extends SuperBO {

    void getAllShopCategories();

    void getShopCategory(int categoryId);

    void saveShopCategory(String categoryName);

    void updateShopCategory(int categoryId);
}
