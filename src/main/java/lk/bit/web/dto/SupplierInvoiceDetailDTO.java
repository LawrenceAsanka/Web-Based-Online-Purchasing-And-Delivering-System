package lk.bit.web.dto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SupplierInvoiceDetailDTO {

    private String productCode;
    private int totalQty;
    private BigDecimal pricePerQty;
    private BigDecimal discount;
}
