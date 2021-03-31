package lk.bit.web.repository;

import lk.bit.web.entity.PurchaseInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseInvoiceRepository extends JpaRepository<PurchaseInvoice, String> {

    @Query(value = "SELECT id FROM purchase_invoice ORDER BY id DESC LIMIT 1", nativeQuery = true)
    public String getLastPurchaseId();
}
