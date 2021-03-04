package lk.bit.web.api;

import lk.bit.web.business.custom.OrderInvoiceBO;
import lk.bit.web.business.custom.ShopBO;
import lk.bit.web.business.custom.SystemUserBO;
import lk.bit.web.dto.OrderInvoiceDTO;
import lk.bit.web.util.tm.AssignOrderInvoiceDetailTM;
import lk.bit.web.util.tm.AssignOrderInvoiceTM;
import lk.bit.web.util.tm.OrderInvoiceTM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1/orderInvoices")
public class OrderInvoiceController {
    @Autowired
    private OrderInvoiceBO orderInvoiceBO;
    @Autowired
    private ShopBO shopBO;
    @Autowired
    private SystemUserBO systemUserBO;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    private List<OrderInvoiceDTO> readOrderInvoiceDetailByStatus() {
        return orderInvoiceBO.readOrderInvoiceDetailByStatus();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    private List<OrderInvoiceTM> readOrderInvoiceDetailByOrderId(@PathVariable String id) {
        return orderInvoiceBO.readOrderInvoiceDetailByOrderId(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/confirm")
    private List<OrderInvoiceDTO> readOrderInvoiceDetailsByStatusConfirm() {
        return orderInvoiceBO.readOrderInvoiceByStatusConfirm();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/cancel")
    private List<OrderInvoiceDTO> readOrderInvoiceDetailsByStatusCancel() {
        return orderInvoiceBO.readOrderInvoiceByStatusCancel();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/complete")
    private List<OrderInvoiceDTO> readOrderInvoiceDetailsByStatusComplete() {
        return orderInvoiceBO.readOrderInvoiceByStatusComplete();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/process")
    private List<OrderInvoiceDTO> readOrderInvoiceDetailsByStatusProcess() {
        return orderInvoiceBO.readOrderInvoiceByStatusProcess();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/delivery")
    private List<AssignOrderInvoiceTM> readOrderInvoiceDetailsByStatusDelivery() {
        return orderInvoiceBO.readOrderInvoiceByStatusDelivery();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/customer")
    private List<OrderInvoiceDTO> readOrderInvoiceDetailsByCustomerId(@RequestParam("id") String customerId) {
        return orderInvoiceBO.readOrderInvoiceByCustomerId(customerId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/assignee")
    private List<AssignOrderInvoiceDetailTM> readOrderInvoiceDetailsByAssignee(@RequestParam("userName") String assigneeName) {
        if (!systemUserBO.existUser(assigneeName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return orderInvoiceBO.readOrderInvoiceByAssignee(assigneeName);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/count")
    private int getTotalConfirmOrderCount() {
        return orderInvoiceBO.getTotalConfirmOrderCount();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/orderDetails")
    private void getOrderDetail(@RequestParam String orderId, @RequestParam String assignee) {

        if (!orderId.equals(orderInvoiceBO.getOrderIdFromAssignOrder(assignee,orderId ))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        System.out.println("ok");
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    private void save(@RequestBody OrderInvoiceDTO orderInvoiceDTO) {
        if (orderInvoiceDTO == null || !shopBO.existShopById(orderInvoiceDTO.getShopId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        orderInvoiceBO.saveOrder(orderInvoiceDTO);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/moveToDelivery")
    private void updateOrderStatusToDelivery(@RequestParam("orderIdArray") String orderIdArray,
                                             @RequestParam String assignedTo) throws IOException {
        if (orderIdArray == null || assignedTo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        orderInvoiceBO.updateStatusToDelivery(orderIdArray,assignedTo );
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}/cancel")
    private void updateOrderStatus(@PathVariable String id){
        if (id == null || !orderInvoiceBO.IExistOrderByOrderId(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        orderInvoiceBO.updateStatus(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/moveToProcess")
    private void updateOrderStatusToProcess(@RequestParam("orderIdArray") String orderIdArray){
        if (orderIdArray == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        orderInvoiceBO.updateStatusToProcess(orderIdArray);
    }

}
