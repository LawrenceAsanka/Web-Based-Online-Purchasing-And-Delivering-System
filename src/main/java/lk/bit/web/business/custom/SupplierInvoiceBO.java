package lk.bit.web.business.custom;

import lk.bit.web.dto.SupplierInvoiceDTO;
import lk.bit.web.dto.SupplierInvoiceDetailDTO;
import lk.bit.web.entity.SuperEntity;
import lk.bit.web.util.InvoiceDetailTM;

import java.util.List;

public interface SupplierInvoiceBO extends SuperEntity {

    void saveSupplierInvoice(SupplierInvoiceDTO invoiceDetails);

    List<InvoiceDetailTM> getAllInvoiceDetails();

    boolean existInvoice(String invoiceNumber);

    List<SupplierInvoiceDetailDTO> getInvoiceDetail(String invoiceNumber);
}
