package lk.bit.web.repository;

import lk.bit.web.entity.CustomEntity3;
import lk.bit.web.entity.SupplierInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SupplierInvoiceRepository extends JpaRepository<SupplierInvoice, String> {

    @Query(value = "SELECT s.invoiceNumber AS invoiceNumber,s.dateAndTime AS dateTime,s.systemUser.id AS userId," +
            "s.systemUser.firstName AS userName,s.remarks AS remarks," +
            "SUM(((100 - sd.discount)/100) * (sd.qty * sd.qtyPrice)) AS netAmount " +
            "FROM SupplierInvoiceDetail sd  INNER JOIN sd.supplierInvoice s GROUP BY s.invoiceNumber " +
            "ORDER BY s.invoiceNumber")
    List<CustomEntity3> getAllInvoiceDetails();
/*
    @Query(value = "SELECT SI.invoice_id AS invoiceNumber,SI.created_date_time AS dateTime, " +
            "SUM(((100 - SD.discount)/100) * (SD.qty * SD.qty_price)) AS netAmount " +
            "FROM supplier_invoice_detail SD INNER JOIN supplier_invoice SI on SD.supplier_invoice_id = SI.invoice_id " +
            "WHERE DATE(SI.created_date_time) >= ?1 AND DATE(SI.created_date_time) <= ?2 "+
            "GROUP BY SI.invoice_id ORDER BY SI.invoice_id", nativeQuery = true)*/
/*@Query(value = "SELECT SI.invoiceNumber AS invoiceNumber FROM SupplierInvoice SI")
    List<CustomEntity3> readDetailsByDateRange(String startDate, String endDate);*/
}
