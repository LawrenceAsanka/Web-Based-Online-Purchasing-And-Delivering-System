package lk.bit.web.util.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PurchaseDetailTM {

    private String purchaseId;
    private String purchasedDateTime;
    private String productId;
    private String product;
    private String Unit;
    private String totalQty;
}
