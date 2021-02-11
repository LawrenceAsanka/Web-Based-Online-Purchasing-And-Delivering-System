package lk.bit.web.business.custom;

import lk.bit.web.business.SuperBO;
import lk.bit.web.dto.OrderInvoiceDTO;

public interface OrderInvoiceBO extends SuperBO{

    void saveOrder(OrderInvoiceDTO orderInvoiceDTO);
    void autoConfirmOrder();
}
