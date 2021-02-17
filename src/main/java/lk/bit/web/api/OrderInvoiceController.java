package lk.bit.web.api;

import lk.bit.web.business.custom.CustomerBO;
import lk.bit.web.business.custom.OrderInvoiceBO;
import lk.bit.web.business.custom.ShopBO;
import lk.bit.web.dto.OrderInvoiceDTO;
import lk.bit.web.util.OrderInvoiceTM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1/orderInvoices")
public class OrderInvoiceController {
    @Autowired
    private OrderInvoiceBO orderInvoiceBO;
    @Autowired
    private ShopBO shopBO;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    private List<OrderInvoiceDTO> readOrderInvoiceDetailByStatus(){
        return orderInvoiceBO.readOrderInvoiceDetailByStatus();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    private List<OrderInvoiceTM> readOrderInvoiceDetailByOrderId(@PathVariable String id){
        return orderInvoiceBO.readOrderInvoiceDetailByOrderId(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    private void save(@RequestBody OrderInvoiceDTO orderInvoiceDTO) {
        if (orderInvoiceDTO == null || !shopBO.existShopById(orderInvoiceDTO.getShopId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        orderInvoiceBO.saveOrder(orderInvoiceDTO);
    }

}
