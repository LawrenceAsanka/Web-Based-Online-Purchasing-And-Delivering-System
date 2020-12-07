package lk.bit.web.api;

import lk.bit.web.dto.SupplierInvoiceDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/v1/supplierInvoices")
public class SupplierInvoiceController {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    private void saveSupplierInvoice(@RequestBody SupplierInvoiceDTO invoiceDetails){
        System.out.println(invoiceDetails);
    }
}
