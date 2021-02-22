package lk.bit.web.repository;

import lk.bit.web.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShopRepository extends JpaRepository<Shop, String> {

    @Query(value = "SELECT id FROM shop ORDER BY id DESC LIMIT 1", nativeQuery = true)
    String getLastShopId();

    @Query(value = "SELECT s FROM Shop s WHERE s.customerUser.customerId = ?1 AND s.isActive = 1")
    List<Shop> getActiveShopDetailsByCustomer(String customerId);

    @Query(value = "SELECT s FROM Shop s WHERE s.customerUser.customerId = ?1 ORDER BY s.shopId")
    List<Shop> getAllShopDetailsByCustomerId(String customerId);

    @Query(value = "SELECT count(*) FROM shop WHERE is_active=1", nativeQuery = true)
    public int getTotalActiveShopCount();
}
