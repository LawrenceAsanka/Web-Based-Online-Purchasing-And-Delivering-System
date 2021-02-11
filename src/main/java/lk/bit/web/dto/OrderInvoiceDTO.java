package lk.bit.web.dto;

import lombok.*;

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
    private List<OrderInvoiceDetailDTO> orderInvoiceDetail;
}
