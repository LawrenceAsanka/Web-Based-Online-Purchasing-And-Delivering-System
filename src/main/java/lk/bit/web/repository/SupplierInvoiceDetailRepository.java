package lk.bit.web.repository;

import lk.bit.web.entity.SupplierInvoiceDetail;
import lk.bit.web.entity.SupplierInvoiceDetailPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SupplierInvoiceDetailRepository extends JpaRepository<SupplierInvoiceDetail, SupplierInvoiceDetailPK> {

    @Query(value = "SELECT sd FROM SupplierInvoiceDetail sd WHERE sd.supplierInvoiceDetailPK.invoiceNumber = ?1  " +
            "ORDER BY sd.supplierInvoiceDetailPK.invoiceNumber")
    List<SupplierInvoiceDetail> getInvoiceDetail(String invoiceNumber);
}
