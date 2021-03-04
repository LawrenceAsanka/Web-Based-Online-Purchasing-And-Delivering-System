package lk.bit.web.util.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AssignOrderInvoiceDetailTM {

    private String orderId;
    private String orderCreatedDateTime;
    private String assignedDateTime;
    private String customerName;
    private String shopName;
    private String netTotal;
}
