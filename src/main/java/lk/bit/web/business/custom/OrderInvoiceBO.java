package lk.bit.web.business.custom;

import lk.bit.web.business.SuperBO;
import lk.bit.web.dto.OrderInvoiceDTO;
import lk.bit.web.util.OrderInvoiceTM;

import java.util.List;

public interface OrderInvoiceBO extends SuperBO{

    void saveOrder(OrderInvoiceDTO orderInvoiceDTO);
    List<OrderInvoiceDTO> readOrderInvoiceDetailByStatus();
    List<OrderInvoiceTM> readOrderInvoiceDetailByOrderId(String orderId);
    List<OrderInvoiceDTO> readOrderInvoiceByStatusConfirm();
    List<OrderInvoiceDTO> readOrderInvoiceByStatusCancel();
    List<OrderInvoiceDTO> readOrderInvoiceByCustomerId(String customerId);
    void updateStatus(String orderId);
    boolean IExistOrderByOrderId(String id);
    public int getTotalConfirmOrderCount();
}
