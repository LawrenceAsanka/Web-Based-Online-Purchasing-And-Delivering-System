package lk.bit.web.api;

import lk.bit.web.business.custom.SupplierInvoiceBO;
import lk.bit.web.business.custom.UserBO;
import lk.bit.web.dto.SupplierInvoiceDTO;
import lk.bit.web.util.InvoiceDetailTM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1/supplierInvoices")
public class SupplierInvoiceController {

    @Autowired
    private UserBO userBO;
    @Autowired
    private SupplierInvoiceBO supplierInvoiceBO;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/")
    private List<InvoiceDetailTM> getAllInvoiceDetails(){
        return supplierInvoiceBO.getAllInvoiceDetails();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    private List<SupplierInvoiceDTO> getInvoiceDetail(@RequestParam("invoice") String invoiceNumber){
        if (!supplierInvoiceBO.existInvoice(invoiceNumber)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    private void saveSupplierInvoice(@RequestBody SupplierInvoiceDTO invoiceDetails){
        System.out.println(invoiceDetails);
        if (!userBO.existUser(invoiceDetails.getUserId()) || invoiceDetails.getInvoiceNumber() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
       supplierInvoiceBO.saveSupplierInvoice(invoiceDetails);
    }

}
