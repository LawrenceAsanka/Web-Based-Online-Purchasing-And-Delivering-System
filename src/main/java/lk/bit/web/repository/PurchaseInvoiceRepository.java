package lk.bit.web.repository;

import lk.bit.web.entity.CustomEntity15;
import lk.bit.web.entity.PurchaseInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseInvoiceRepository extends JpaRepository<PurchaseInvoice, String> {

    @Query(value = "SELECT id FROM purchase_invoice ORDER BY id DESC LIMIT 1", nativeQuery = true)
    public String getLastPurchaseId();

    @Query(value = "SELECT PID.product.productId AS productId, PID.product.productName AS productName, " +
            "PID.product.weight AS productWeight, PID.purchaseQuantity AS purchaseQty, PID.product.quantityPerUnit AS qtyPerUnit, " +
            "PI.id AS purchaseId, PI.purchaseDateTime AS dateTime FROM PurchaseInvoiceDetail PID INNER JOIN PID.purchaseInvoice PI " +
            "WHERE PI.id = ?1")
    public List<CustomEntity15> readAllPurchaseDetailsById(String purchaseId);
}
