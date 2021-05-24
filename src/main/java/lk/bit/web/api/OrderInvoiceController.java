package lk.bit.web.api;

import lk.bit.web.business.custom.OrderInvoiceBO;
import lk.bit.web.business.custom.ShopBO;
import lk.bit.web.business.custom.SystemUserBO;
import lk.bit.web.dto.DeliveryOrderDTO;
import lk.bit.web.dto.OrderInvoiceDTO;
import lk.bit.web.util.tm.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
    private List<OrderInvoiceDTO> readOrderInvoiceDetailByStatus(@RequestParam(name = "email", required = false) String customerEmail) {
        if (customerEmail != null) {
            return orderInvoiceBO.readOrderInvoiceDetailByStatus(customerEmail);
        } else {
            return orderInvoiceBO.readOrderInvoiceDetailByStatus();
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    private List<OrderInvoiceTM> readOrderInvoiceDetailByOrderId(@PathVariable String id) {
        return orderInvoiceBO.readOrderInvoiceDetailByOrderId(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/confirm")
    private List<OrderInvoiceDTO> readOrderInvoiceDetailsByStatusConfirm(@RequestParam(name = "email", required = false) String customerEmail) {
        if (customerEmail != null) {
            return orderInvoiceBO.readOrderInvoiceByStatusConfirm(customerEmail);
        } else {
            return orderInvoiceBO.readOrderInvoiceByStatusConfirm();
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/cancel")
    private List<OrderInvoiceDTO> readOrderInvoiceDetailsByStatusCancel(@RequestParam(name = "email", required = false) String customerEmail) {
        if (customerEmail != null) {
            return orderInvoiceBO.readOrderInvoiceByStatusCancel(customerEmail);
        } else {
            return orderInvoiceBO.readOrderInvoiceByStatusCancel();
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/completeWithCustomer")
    private List<OrderInvoiceDTO> readOrderInvoiceDetailsByStatusComplete(@RequestParam("email") String customerEmail) {

        return orderInvoiceBO.readOrderInvoiceByStatusComplete(customerEmail);
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
    @GetMapping("/complete")
    private List<CompleteDeliveryDetailTM> readOrderInvoiceDetailsByStatusComplete() {

        return orderInvoiceBO.readOrderInvoiceByStatusComplete();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/customer")
    private List<OrderInvoiceDTO> readOrderInvoiceDetailsByCustomerId(@RequestParam("id") String customerId) {
        return orderInvoiceBO.readOrderInvoiceByCustomerId(customerId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/assignee")
    private List<AssignOrderInvoiceDetailTM> readOrderInvoiceDetailsByAssignee(@RequestParam("userName") String assigneeName) {
        /*if (!systemUserBO.existUser(assigneeName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }*/
        return orderInvoiceBO.readOrderInvoiceByAssignee(assigneeName);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/count")
    private int getTotalConfirmOrderCount() {
        return orderInvoiceBO.getTotalConfirmOrderCount();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/orderDetails")
    private DeliveryOrderDTO getOrderDetail(@RequestParam String orderId, @RequestParam String assignee) {

        if (!orderId.equals(orderInvoiceBO.getOrderIdFromAssignOrder(assignee,orderId ))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return orderInvoiceBO.getDeliveryOrderDetail(orderId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/completedOrderDetails")
    private List<CompleteDeliveryDetailTM2> getCompleteOrderDetailsByAssignee(@RequestParam("userName") String assignee) {

        /*if (!systemUserBO.existUser(assignee)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }*/
        return orderInvoiceBO.getCompletedOrderDetailsByAssignee(assignee);
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/orderStatus")
    private int getOrderStatusByOrderId(@RequestParam("id") String orderId) {

        if (!orderInvoiceBO.IsExistOrderByOrderId(orderId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return orderInvoiceBO.getOrderStatusByOrderId(orderId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/filterByDates")
    private List<OrderInvoiceDTO> readOrderInvoiceDetailsByStatusCancelAndDate(@RequestParam("startDate") String startDate,
                                                                               @RequestParam("endDate") String endDate) {
        if (startDate == null && endDate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return orderInvoiceBO.readOrderInvoiceByStatusCancelAndDate(startDate, endDate);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/filterByOrderDates")
    private List<CompleteDeliveryDetailTM> readDetailsByOrderedDateRange(@RequestParam("startDate") String startDate,
                                                         @RequestParam("endDate") String endDate){
        if (startDate == null && endDate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return orderInvoiceBO.readOrderInvoiceByStatusCompleteAndOrderedDate(startDate, endDate);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/filterByDeliveredDates")
    private List<CompleteDeliveryDetailTM> readDetailsByDeliveredDateRange(@RequestParam("startDate") String startDate,
                                                                  @RequestParam("endDate") String endDate){
        if (startDate == null && endDate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return orderInvoiceBO.readOrderInvoiceByStatusCompleteAndDeliveredDate(startDate, endDate);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    private String saveOrder(@RequestBody OrderInvoiceDTO orderInvoiceDTO) {
        String orderId= null;
        if (orderInvoiceDTO.getNetTotal() == null || !shopBO.existShopById(orderInvoiceDTO.getShopId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (orderInvoiceDTO.getPaymentMethod().equals("1")) {
            orderId = orderInvoiceBO.saveOrderByCOD(orderInvoiceDTO);
        } else {
           orderId =  orderInvoiceBO.saveOrderByCredit(orderInvoiceDTO);
        }

        return orderId;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path= "/credit",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    private void saveCreditOrderProve(@RequestParam("customer") String customerEmail,
                                   @RequestParam("orderId") String orderId,
                                   @RequestParam("nicFrontImage") MultipartFile nicFrontImage,
                                   @RequestParam("nicBackImage") MultipartFile nicBackImage) {
        if (customerEmail == null || nicFrontImage.isEmpty() || nicBackImage.isEmpty() || orderId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        orderInvoiceBO.saveCreditProve(customerEmail, orderId, nicFrontImage, nicBackImage);
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
    @PostMapping("/completeOrder")
    private void saveCompleteDeliveryDetails(@RequestParam("id") String orderId){
        if (orderId == null || !orderInvoiceBO.IsExistOrderByOrderId(orderId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        orderInvoiceBO.saveCompleteDeliveryDetails(orderId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}/cancel")
    private void updateStatusToCancel(@PathVariable String id){
        if (id == null || !orderInvoiceBO.IsExistOrderByOrderId(id)) {
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
