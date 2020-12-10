package lk.bit.web.repository;

import lk.bit.web.entity.CustomEntity3;
import lk.bit.web.entity.SupplierInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SupplierInvoiceRepository extends JpaRepository<SupplierInvoice, String> {

    @Query(value = "SELECT s.invoiceNumber AS invoiceNumber,s.dateAndTime AS dateTime,s.user.id AS userId," +
            "s.user.firstName AS userName,SUM(sd.qty * sd.qtyPrice) AS netAmount FROM SupplierInvoiceDetail sd  INNER JOIN " +
            "sd.supplierInvoice s GROUP BY s.invoiceNumber ORDER BY s.invoiceNumber")
    List<CustomEntity3> getAllInvoiceDetails();

}
