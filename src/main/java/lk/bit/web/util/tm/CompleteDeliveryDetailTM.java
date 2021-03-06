package lk.bit.web.util.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CompleteDeliveryDetailTM {

    private String orderId;
    private String orderedDateTime;
    private String orderBy;
    private String deliverBy;
    private String deliveredDateTime;
    private String paymentMethod;
    private String status;
}
