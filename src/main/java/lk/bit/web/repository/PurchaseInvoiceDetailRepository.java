package lk.bit.web.repository;

import lk.bit.web.entity.PurchaseInvoiceDetail;
import lk.bit.web.entity.PurchaseInvoiceDetailPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseInvoiceDetailRepository extends JpaRepository<PurchaseInvoiceDetail, PurchaseInvoiceDetailPK> {
}
