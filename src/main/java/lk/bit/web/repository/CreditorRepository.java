package lk.bit.web.repository;

import lk.bit.web.entity.CreditDetail;
import lk.bit.web.entity.CustomEntity13;
import lk.bit.web.entity.CustomEntity14;
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
            "INNER JOIN order_invoice OI on CD.order_id = OI.order_id WHERE AC.assignee_id=?1 AND CD.is_settle=0", nativeQuery = true)
    List<CustomEntity13> readCreditDetailsByAssignee(String assignee);

    @Query(value = "SELECT CD.id AS creditId, CD.order_id AS orderId, CD.credit_date AS creditDate, " +
            "CD.customer_id AS creditor, CD.last_date_to_settle AS settleDay, " +
            "CD.total_credit_amount AS creditAmount, AC.assignee_id AS assignee " +
            "FROM creditor_detail CD INNER JOIN assigned_credit AC on CD.id = AC.credit_id " +
            "WHERE is_settle=1", nativeQuery = true)
    List<CustomEntity14> readAllCompleteCreditCollectionDetails();
}
