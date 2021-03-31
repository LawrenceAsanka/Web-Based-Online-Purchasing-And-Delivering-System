package lk.bit.web.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PurchaseInvoiceDTO {

    private String purchaseId;
    private String purchaseDateTime;
}
