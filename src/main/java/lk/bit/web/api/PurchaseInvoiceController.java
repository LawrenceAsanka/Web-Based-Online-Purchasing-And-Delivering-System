package lk.bit.web.api;

import lk.bit.web.business.custom.PurchaseInvoiceBO;
import lk.bit.web.dto.PurchaseInvoiceDTO;
import lk.bit.web.util.tm.PurchaseDetailTM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1/purchaseDetails")
public class PurchaseInvoiceController {
    @Autowired
    private PurchaseInvoiceBO purchaseInvoiceBO;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    private List<PurchaseInvoiceDTO> readAllPurchaseDetails(){
        return purchaseInvoiceBO.readAllPurchaseDetails();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    private List<PurchaseDetailTM> readAllPurchaseDetailsById(@PathVariable("id") String purchaseId){
        return purchaseInvoiceBO.readPurchaseDetailsById(purchaseId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    private void deletePurchaseDetail(@PathVariable("id") String purchaseId){
        System.out.println(purchaseId);
        purchaseInvoiceBO.deletePurchaseInvoice(purchaseId);
    }

}