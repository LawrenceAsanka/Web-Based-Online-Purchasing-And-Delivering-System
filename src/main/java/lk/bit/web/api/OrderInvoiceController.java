package lk.bit.web.api;

import lk.bit.web.business.custom.CustomerBO;
import lk.bit.web.business.custom.OrderInvoiceBO;
import lk.bit.web.business.custom.ShopBO;
import lk.bit.web.dto.OrderInvoiceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1/orderInvoices")
public class OrderInvoiceController {
    @Autowired
    private OrderInvoiceBO orderInvoiceBO;
    @Autowired
    private CustomerBO customerBO;
    @Autowired
    private ShopBO shopBO;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    private void save(@RequestBody OrderInvoiceDTO orderInvoiceDTO) {
        if (orderInvoiceDTO == null || !shopBO.existShopById(orderInvoiceDTO.getShopId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        orderInvoiceBO.saveOrder(orderInvoiceDTO);
    }
}
