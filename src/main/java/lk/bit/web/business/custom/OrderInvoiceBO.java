package lk.bit.web.business.custom;

import lk.bit.web.business.SuperBO;
import lk.bit.web.dto.DeliveryOrderDTO;
import lk.bit.web.dto.OrderInvoiceDTO;
import lk.bit.web.util.tm.AssignOrderInvoiceDetailTM;
import lk.bit.web.util.tm.AssignOrderInvoiceTM;
import lk.bit.web.util.tm.OrderInvoiceTM;

import java.io.IOException;
import java.util.List;

public interface OrderInvoiceBO extends SuperBO{

    void saveOrder(OrderInvoiceDTO orderInvoiceDTO);
    List<OrderInvoiceDTO> readOrderInvoiceDetailByStatus();
    List<OrderInvoiceTM> readOrderInvoiceDetailByOrderId(String orderId);
    List<OrderInvoiceDTO> readOrderInvoiceByStatusConfirm();
    List<OrderInvoiceDTO> readOrderInvoiceByStatusCancel();
    List<OrderInvoiceDTO> readOrderInvoiceByStatusComplete();
    List<OrderInvoiceDTO> readOrderInvoiceByStatusProcess();
    List<AssignOrderInvoiceTM> readOrderInvoiceByStatusDelivery();
    List<AssignOrderInvoiceDetailTM> readOrderInvoiceByAssignee(String assigneeId);
    List<OrderInvoiceDTO> readOrderInvoiceByCustomerId(String customerId);
    void updateStatus(String orderId);
    void updateStatusToProcess(String orderIdArray);
    void updateOrderStatusToComplete(String orderId);
    void updateStatusToDelivery(String orderIdArray, String assigneeId) throws IOException;
    boolean IsExistOrderByOrderId(String id);
    int getTotalConfirmOrderCount();
    String getOrderIdFromAssignOrder(String assignee, String orderId);
    DeliveryOrderDTO getDeliveryOrderDetail(String orderId);
}
