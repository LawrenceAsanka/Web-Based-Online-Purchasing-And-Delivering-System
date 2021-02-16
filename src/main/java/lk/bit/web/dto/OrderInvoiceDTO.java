package lk.bit.web.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class OrderInvoiceDTO {

    private String orderId;
    private String customerId;
    private String shopId;
    private BigDecimal netTotal;
    private List<OrderInvoiceDetailDTO> orderInvoiceDetail;
}
