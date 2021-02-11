package lk.bit.web.dto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class OrderInvoiceDetailDTO {

    private String productId;
    private BigDecimal discount;
    private int quantity;
    private BigDecimal total;
}
