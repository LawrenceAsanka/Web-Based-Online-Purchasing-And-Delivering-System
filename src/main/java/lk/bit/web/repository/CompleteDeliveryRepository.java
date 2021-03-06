package lk.bit.web.repository;

import lk.bit.web.entity.CompleteDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CompleteDeliveryRepository extends JpaRepository<CompleteDelivery, String> {

    @Query(value = "SELECT id FROM complete_delivery ORDER BY id DESC LIMIT 1", nativeQuery = true)
    public String getCompleteDeliveryId();
}
