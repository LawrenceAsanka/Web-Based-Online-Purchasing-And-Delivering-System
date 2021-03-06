package lk.bit.web.repository;

import lk.bit.web.entity.CustomEntity16;
import lk.bit.web.entity.CustomEntity5;
import lk.bit.web.entity.OrderInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderInvoiceRepository extends JpaRepository<OrderInvoice, String> {

    @Query(value = "SELECT OI.order_id FROM order_invoice OI ORDER BY order_id DESC LIMIT 1" , nativeQuery = true)
    public String getOrderId();

    @Query(value = "SELECT OI.orderId AS orderId, OI.createdDateAndTime AS orderDateTime, OI.netTotal AS netTotal ,OI.customerUser.customerId AS customerId, S.shopName AS shopName," +
            "OI.paymentMethod AS paymentMethod, S.address1 AS address1, S.address2 AS address2, S.city AS city, S.district AS district, S.contact AS contact, " +
            "ODI.product.productId AS productId, ODI.product.productName AS productName, ODI.product.imageOne AS productImage, ODI.discount AS discount, " +
            "ODI.quantity AS quantity, ODI.total AS total FROM OrderInvoiceDetail ODI INNER JOIN OrderInvoice OI ON " +
            "ODI.orderInvoiceDetailPK.orderInvoiceId = OI.orderId INNER JOIN OI.shop S WHERE OI.orderId=?1")
    public List<CustomEntity5> getOrderInvoice(String orderId);

    @Query(value = "SELECT * FROM order_invoice WHERE status=0 AND customer_id=?1 ORDER BY order_id", nativeQuery = true)
    public List<OrderInvoice> getOrderInvoiceByStatus(String customerId);

    @Query(value = "SELECT * FROM order_invoice WHERE status=0 ORDER BY order_id", nativeQuery = true)
    public List<OrderInvoice> getOrderInvoiceByStatus();

    @Query(value = "SELECT * FROM order_invoice WHERE status=1 AND customer_id=?1 ORDER BY order_id", nativeQuery = true)
    public List<OrderInvoice> readOrderInvoiceByOrderStatusCancel(String customerId);

    @Query(value = "SELECT * FROM order_invoice WHERE status=1 ORDER BY order_id", nativeQuery = true)
    public List<OrderInvoice> readOrderInvoiceByOrderStatusCancel();

    @Query(value = "SELECT * FROM order_invoice WHERE status=2 AND customer_id=?1 ORDER BY order_id", nativeQuery = true)
    public List<OrderInvoice> readOrderInvoiceByOrderStatusConfirm(String customerId);

    @Query(value = "SELECT * FROM order_invoice WHERE status=2 ORDER BY order_id", nativeQuery = true)
    public List<OrderInvoice> readOrderInvoiceByOrderStatusConfirm();

    @Query(value = "SELECT * FROM order_invoice WHERE status=3 ORDER BY order_id", nativeQuery = true)
    public List<OrderInvoice> readOrderInvoiceByOrderStatusProcess();

    @Query(value = "SELECT * FROM order_invoice WHERE status=4 ORDER BY order_id", nativeQuery = true)
    public List<OrderInvoice> readOrderInvoiceByOrderStatusDelivery();

    @Query(value = "SELECT * FROM order_invoice WHERE status=5 AND customer_id=?1 ORDER BY order_id", nativeQuery = true)
    public List<OrderInvoice> readOrderInvoiceByOrderStatusComplete(String customerId);

    @Query(value = "SELECT OI FROM OrderInvoice OI WHERE OI.customerUser.customerId=?1 order by OI.orderId")
    public List<OrderInvoice> getOrderInvoiceByCustomerId(String customerId);

    @Query(value = "SELECT COUNT(*) FROM order_invoice WHERE status=2 ", nativeQuery = true)
    public int getTotalConfirmOrderCount();

    @Query(value = "SELECT COUNT(*) FROM order_invoice WHERE status=0 ", nativeQuery = true)
    public int getTotalNewOrderCount();

    @Query(value = "SELECT SUM(net_total) FROM order_invoice WHERE customer_id=?1", nativeQuery = true)
    public String getTotalNetTotalByCustomer(String customerId);

    @Query(value = "SELECT UCASE(MONTHNAME(OI.created_date_time)) AS month, " +
            "SUM(OI.net_total) AS netTotal FROM order_invoice OI WHERE OI.status = 5 AND YEAR(OI.created_date_time)=?1 " +
            "GROUP BY UCASE(MONTHNAME(OI.created_date_time))", nativeQuery = true)
    public List<CustomEntity16> readNetTotalByMonth(String year);

    @Query(value = "SELECT SUM(OI.net_total) FROM order_invoice OI INNER JOIN assign_order_invoice_detail AOI " +
            "on OI.order_id = AOI.order_invoice_id INNER JOIN complete_delivery CD " +
            "on AOI.id = CD.assign_invoice_id WHERE DATE(CD.delivered_date_time) = ?1",
            nativeQuery = true)
    public BigDecimal readTotalNetAmountByDate(String date);

    @Query(value = "SELECT SUM(OI.net_total) FROM order_invoice OI INNER JOIN assign_order_invoice_detail AOI " +
            "on OI.order_id = AOI.order_invoice_id INNER JOIN complete_delivery CD " +
            "on AOI.id = CD.assign_invoice_id WHERE DATE(CD.delivered_date_time) = ?1 AND OI.payment_method = ?2",
            nativeQuery = true)
    public BigDecimal readTotalCreditAmount(String date, String paymentMethod);

    @Query(value = "SELECT SUM(OI.net_total) FROM order_invoice OI INNER JOIN assign_order_invoice_detail AOI " +
            "on OI.order_id = AOI.order_invoice_id INNER JOIN complete_delivery CD " +
            "on AOI.id = CD.assign_invoice_id WHERE DATE(CD.delivered_date_time) = ?1 AND OI.payment_method = ?2",
            nativeQuery = true)
    public BigDecimal readTotalCODAmount(String date, String paymentMethod);
}
