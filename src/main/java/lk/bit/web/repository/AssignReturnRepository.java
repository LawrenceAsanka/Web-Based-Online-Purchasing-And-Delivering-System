package lk.bit.web.repository;

import lk.bit.web.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignReturnRepository extends JpaRepository<AssignReturn, Integer> {

    /*
    @Query(value = "SELECT AO.order_invoice_id FROM assign_order_invoice_detail AO WHERE AO.assignee_id=?1 AND AO.order_invoice_id=?2", nativeQuery = true)
    public String getOrderIdFromAssignOrder(String assigneeId, String orderId);

    @Query(value = "SELECT AO.* FROM assign_order_invoice_detail AO WHERE AO.order_invoice_id=?1", nativeQuery = true)
    public AssignOrderInvoiceDetail getAssignInvoiceIdByOrderId(String orderId);*/

    @Query(value = "SELECT * FROM assigned_return WHERE return_id=?1",nativeQuery = true)
    public AssignReturn readAssignReturnDetailByReturnId(String returnId);

    @Query(value = "SELECT AR.assignedDateTime AS assignedDateTime, R.orderId.orderId AS orderId, " +
            "R.id AS returnId FROM AssignReturn AR INNER JOIN Return R ON AR.returnId.id = R.id " +
            "WHERE R.status=3 AND AR.assigneeId.id=?1 ORDER BY AR.id")
    public List<CustomEntity11> readAssignReturnDetailByAssignee(String assignee);

    @Query(value = "SELECT COUNT(AR.return_id) FROM assigned_return AR " +
            "WHERE DATE(AR.assigned_date_time) = ?1", nativeQuery = true)
    public int readAllTodayReturnCount(String dateNow);
}
