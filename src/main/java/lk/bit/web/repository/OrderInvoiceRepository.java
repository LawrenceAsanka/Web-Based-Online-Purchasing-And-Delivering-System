package lk.bit.web.repository;

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
}
