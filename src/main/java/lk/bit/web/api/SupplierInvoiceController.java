package lk.bit.web.api;

import lk.bit.web.business.custom.UserBO;
import lk.bit.web.dto.SupplierInvoiceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin
@RestController
@RequestMapping("api/v1/supplierInvoices")
public class SupplierInvoiceController {

    @Autowired
    private UserBO userBO;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    private void saveSupplierInvoice(@RequestBody SupplierInvoiceDTO invoiceDetails){
        if (!userBO.existUser(invoiceDetails.getUserId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        System.out.println(invoiceDetails);
    }
}
