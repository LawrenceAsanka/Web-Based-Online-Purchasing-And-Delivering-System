package lk.bit.web.util.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ReturnInvoiceTM {

    private String productId;
    private String productName;
    private String productImagePath;
    private int returnQty;
    private String reasonToReturn;
}
