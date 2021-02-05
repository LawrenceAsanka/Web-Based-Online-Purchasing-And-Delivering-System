package lk.bit.web.repository;

import lk.bit.web.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShopRepository extends JpaRepository<Shop, String> {

    @Query(value = "SELECT id FROM shop ORDER BY id DESC LIMIT 1", nativeQuery = true)
    String getLastShopId();
}
