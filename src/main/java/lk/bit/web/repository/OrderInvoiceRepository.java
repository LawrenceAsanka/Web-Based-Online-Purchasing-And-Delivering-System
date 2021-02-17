package lk.bit.web.repository;

import lk.bit.web.entity.CustomEntity5;
import lk.bit.web.entity.OrderInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderInvoiceRepository extends JpaRepository<OrderInvoice, String> {

    @Query(value = "SELECT OI.order_id FROM order_invoice OI ORDER BY order_id DESC LIMIT 1" , nativeQuery = true)
    public String getOrderId();

    @Query(value = "SELECT * FROM order_invoice WHERE status=0", nativeQuery = true)
    public List<OrderInvoice> getOrderInvoiceByStatus();

    @Query(value = "SELECT OI.orderId AS orderId, OI.createdDateAndTime AS orderDateTime, OI.netTotal AS netTotal ,S.shopName AS shopName," +
            "S.address1 AS address1, S.address2 AS address2, S.city AS city, S.district AS district, ODI.product.productId AS productId, " +
            "ODI.product.imageOne, ODI.discount, ODI.quantity, ODI.total FROM OrderInvoiceDetail ODI INNER JOIN OrderInvoice OI ON " +
            "ODI.orderInvoiceDetailPK.orderInvoiceId = OI.orderId INNER JOIN OI.shop S WHERE OI.orderId=?1")
    public List<CustomEntity5> readAllOrderInvoiceDetailsById(String orderId);
}
