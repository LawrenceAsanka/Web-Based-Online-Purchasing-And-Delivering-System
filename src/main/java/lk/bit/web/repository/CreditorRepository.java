package lk.bit.web.repository;

import lk.bit.web.entity.CreditDetail;
import lk.bit.web.entity.CustomEntity13;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditorRepository extends JpaRepository<CreditDetail, String> {

    @Query(value = "SELECT id FROM creditor_detail ORDER BY id DESC LIMIT 1", nativeQuery = true)
    String getLastCreditorId();

    @Query(value = "SELECT COUNT(*) FROM creditor_detail WHERE is_settle=0 AND customer_id=?1", nativeQuery = true)
    int getCountOfNotSettleCreditByCustomer(String customerId);

    @Query(value = "SELECT CD.id AS creditId, OI.order_id AS orderId FROM creditor_detail CD INNER JOIN assigned_credit AC on CD.id = AC.credit_id " +
            "INNER JOIN order_invoice OI on CD.order_id = OI.order_id WHERE AC.assignee_id=?1", nativeQuery = true)
    List<CustomEntity13> readCreditDetailsByAssignee(String assignee);
}
