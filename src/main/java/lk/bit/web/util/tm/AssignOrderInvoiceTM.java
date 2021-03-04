package lk.bit.web.util.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AssignOrderInvoiceTM {

    private String orderId;
    private String orderCreatedDateTime;
    private String assignedDateTime;
    private String customerId;
    private String assignee;
    private String shopId;
    private String netTotal;
    private int orderStatus;
}
