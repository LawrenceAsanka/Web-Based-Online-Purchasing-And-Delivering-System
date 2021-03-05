package lk.bit.web.repository;

import lk.bit.web.entity.CompleteDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompleteDeliveryRepository extends JpaRepository<CompleteDelivery, Integer> {
}
