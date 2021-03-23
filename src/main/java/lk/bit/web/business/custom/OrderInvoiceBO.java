package lk.bit.web.business.custom;

import lk.bit.web.business.SuperBO;
import lk.bit.web.dto.DeliveryOrderDTO;
import lk.bit.web.dto.OrderInvoiceDTO;
import lk.bit.web.util.tm.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface OrderInvoiceBO extends SuperBO{

    void saveOrderByCOD(OrderInvoiceDTO orderInvoiceDTO);
    void saveOrderByCredit(OrderInvoiceDTO orderInvoiceDTO);
    void saveCreditProve(String customerEmail, String netTotal,MultipartFile nicFrontImage, MultipartFile nicBackImage);
    List<OrderInvoiceDTO> readOrderInvoiceDetailByStatus(String customerEmail);
    List<OrderInvoiceDTO> readOrderInvoiceDetailByStatus();
    List<OrderInvoiceTM> readOrderInvoiceDetailByOrderId(String orderId);
    List<OrderInvoiceDTO> readOrderInvoiceByStatusConfirm(String customerEmail);
    List<OrderInvoiceDTO> readOrderInvoiceByStatusConfirm();
    List<OrderInvoiceDTO> readOrderInvoiceByStatusCancel(String customerEmail);
    List<OrderInvoiceDTO> readOrderInvoiceByStatusCancel();
    List<OrderInvoiceDTO> readOrderInvoiceByStatusComplete(String customerEmail);
    List<CompleteDeliveryDetailTM> readOrderInvoiceByStatusComplete();
    List<OrderInvoiceDTO> readOrderInvoiceByStatusProcess();
    List<AssignOrderInvoiceTM> readOrderInvoiceByStatusDelivery();
    List<AssignOrderInvoiceDetailTM> readOrderInvoiceByAssignee(String assigneeId);
    List<OrderInvoiceDTO> readOrderInvoiceByCustomerId(String customerId);
    void updateStatus(String orderId);
    void updateStatusToProcess(String orderIdArray);
    void saveCompleteDeliveryDetails(String orderId);
    void updateStatusToDelivery(String orderIdArray, String assigneeId) throws IOException;
    boolean IsExistOrderByOrderId(String id);
    int getTotalConfirmOrderCount();
    int getOrderStatusByOrderId(String orderId);
    String getOrderIdFromAssignOrder(String assignee, String orderId);
    DeliveryOrderDTO getDeliveryOrderDetail(String orderId);
    List<CompleteDeliveryDetailTM2> getCompletedOrderDetailsByAssignee(String assignee);
}
