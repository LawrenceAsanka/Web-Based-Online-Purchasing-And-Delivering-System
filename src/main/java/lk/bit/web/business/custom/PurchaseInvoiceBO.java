package lk.bit.web.business.custom;

import lk.bit.web.business.SuperBO;
import lk.bit.web.dto.PurchaseInvoiceDTO;
import lk.bit.web.util.tm.PurchaseDetailTM;

import java.util.List;

public interface PurchaseInvoiceBO extends SuperBO {

    public List<PurchaseInvoiceDTO> readAllPurchaseDetails();
    public List<PurchaseDetailTM> readPurchaseDetailsById(String purchaseId);
    public void deletePurchaseInvoice(String purchaseId);
}
