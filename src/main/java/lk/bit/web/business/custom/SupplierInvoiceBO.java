package lk.bit.web.business.custom;

import lk.bit.web.dto.SupplierInvoiceDTO;
import lk.bit.web.entity.SuperEntity;

import java.util.List;

public interface SupplierInvoiceBO extends SuperEntity {

    void saveSupplierInvoice(SupplierInvoiceDTO invoiceDetails);
}
