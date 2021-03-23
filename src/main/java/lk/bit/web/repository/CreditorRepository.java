package lk.bit.web.repository;

import lk.bit.web.entity.Creditor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditorRepository extends JpaRepository<Creditor, String> {

    @Query(value = "SELECT id FROM creditor_detail ORDER BY id DESC LIMIT 1", nativeQuery = true)
    String getLastCreditorId();

    @Query(value = "SELECT COUNT(*) FROM creditor_detail WHERE is_settle=0 AND customer_id=?1", nativeQuery = true)
    int getCountOfNotSettleCreditByCustomer(String customerId);
}
