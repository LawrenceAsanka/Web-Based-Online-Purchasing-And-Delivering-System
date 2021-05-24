package lk.bit.web.api;

import lk.bit.web.business.custom.SupplierInvoiceBO;
import lk.bit.web.business.custom.SystemUserBO;
import lk.bit.web.dto.SupplierInvoiceDTO;
import lk.bit.web.dto.SupplierInvoiceDetailDTO;
import lk.bit.web.util.tm.InvoiceDetailTM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/v1/supplierInvoices")
public class SupplierInvoiceController {

    @Autowired
    private SystemUserBO systemUserBO;
    @Autowired
    private SupplierInvoiceBO supplierInvoiceBO;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/")
    private List<InvoiceDetailTM> getAllInvoiceDetails(){
        return supplierInvoiceBO.getAllInvoiceDetails();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    private List<SupplierInvoiceDetailDTO> getInvoiceDetail(@RequestParam("invoice") String invoiceNumber){
        if (!supplierInvoiceBO.existInvoice(invoiceNumber)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return supplierInvoiceBO.getInvoiceDetail(invoiceNumber);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/filterByDates")
    private List<InvoiceDetailTM> readDetailsByDateRange(@RequestParam("startDate") String startDate,
                                        @RequestParam("endDate") String endDate){
        if (startDate == null && endDate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
       return supplierInvoiceBO.readAllInvoiceDetailsByDateRange(startDate, endDate);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    private void saveSupplierInvoice(@RequestBody SupplierInvoiceDTO invoiceDetails){
        if (invoiceDetails.getInvoiceNumber() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        supplierInvoiceBO.saveSupplierInvoice(invoiceDetails);
    }

}
