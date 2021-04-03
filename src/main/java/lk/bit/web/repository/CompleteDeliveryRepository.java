package lk.bit.web.repository;

import lk.bit.web.entity.CompleteDelivery;
import lk.bit.web.entity.CustomEntity10;
import lk.bit.web.entity.CustomEntity9;
import lk.bit.web.entity.OrderInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompleteDeliveryRepository extends JpaRepository<CompleteDelivery, String> {

    @Query(value = "SELECT id FROM complete_delivery ORDER BY id DESC LIMIT 1", nativeQuery = true)
    public String getCompleteDeliveryId();

    @Query(value = "SELECT AO.assigneeId.id AS deliverBy, CD.deliveredDateTime AS deliveredDateTime, OI.orderId AS orderId," +
            "OI.createdDateAndTime AS orderedDateTime, OI.customerUser.customerId AS orderBy, OI.paymentMethod AS paymentMethod," +
            "OI.status AS status " +
            "FROM AssignOrderInvoiceDetail AO INNER JOIN CompleteDelivery CD ON AO.id = CD.assignInvoiceId.id " +
            "INNER JOIN OrderInvoice OI ON AO.orderInvoiceId.orderId = OI.orderId ORDER BY CD.id")
    public List<CustomEntity9> getAllCompletedDeliveryDetails();

    @Query(value = "SELECT CD.id AS deliveryId, CD.deliveredDateTime AS deliveredDateTime, OI.orderId AS orderId, " +
            "OI.shop.shopName AS shopName, OI.netTotal AS totalAmount, AO.assignedDateTime AS assignedDateTime "+
            "FROM AssignOrderInvoiceDetail AO INNER JOIN CompleteDelivery CD ON AO.id = CD.assignInvoiceId.id " +
            "INNER JOIN OrderInvoice OI ON AO.orderInvoiceId.orderId = OI.orderId WHERE AO.assigneeId.id = ?1 ORDER BY CD.id ")
    public List<CustomEntity10> getAllCompletedDeliveryDetailsByAssignee(String assigneeId);

    @Query(value = "SELECT COUNT(*) FROM complete_delivery CD WHERE DATE(CD.delivered_date_time) = ?1",nativeQuery = true)
    public int readAllTodayDeliveryCount(String dateNow);

}
