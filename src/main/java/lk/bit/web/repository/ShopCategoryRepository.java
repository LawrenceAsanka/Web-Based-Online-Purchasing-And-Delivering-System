package lk.bit.web.repository;

import lk.bit.web.entity.ShopCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShopCategoryRepository extends JpaRepository<ShopCategory, Integer> {

    @Query(value = "SELECT * FROM shop_category WHERE status=1", nativeQuery = true)
    List<ShopCategory> getAllCategoriesByIsActive();

}
