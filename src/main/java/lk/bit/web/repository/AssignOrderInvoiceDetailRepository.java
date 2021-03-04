package lk.bit.web.repository;

import lk.bit.web.entity.AssignOrderInvoiceDetail;
import lk.bit.web.entity.CustomEntity7;
import lk.bit.web.entity.CustomEntity8;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignOrderInvoiceDetailRepository extends JpaRepository<AssignOrderInvoiceDetail, Integer> {

    @Query(value = "SELECT AO.assignedDateTime AS assignedDateTime, AO.assigneeId.id AS assignee, OI.orderId AS orderId," +
            "OI.createdDateAndTime AS orderDateTime, OI.shop.shopId AS shopId, OI.netTotal AS netTotal, OI.status AS orderStatus,OI.customerUser.customerId AS customerId " +
            "FROM AssignOrderInvoiceDetail AO INNER JOIN OrderInvoice OI ON AO.orderInvoiceId.orderId = OI.orderId WHERE OI.status=4 ORDER BY AO.id")
    public List<CustomEntity7> getOrderAssigneeDetails();

    @Query(value = "SELECT AO.assignedDateTime AS assignedDateTime, OI.orderId AS orderId," +
            "OI.createdDateAndTime AS orderDateTime, OI.shop.shopId AS shopId, OI.netTotal AS netTotal, OI.customerUser.customerId AS customerId " +
            "FROM AssignOrderInvoiceDetail AO INNER JOIN OrderInvoice OI ON AO.orderInvoiceId.orderId = OI.orderId WHERE OI.status=4 AND AO.assigneeId.id=?1 ORDER BY AO.id")
    public List<CustomEntity8> getOrderAssigneeDetailsByAssignee(String assignee);
}
