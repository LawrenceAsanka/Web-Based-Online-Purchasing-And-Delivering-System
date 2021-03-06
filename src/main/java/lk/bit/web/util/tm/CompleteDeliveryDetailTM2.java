package lk.bit.web.util.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CompleteDeliveryDetailTM2 {

    private String orderId;
    private String deliveryId;
    private String assignedDateTime;
    private String shop;
    private String deliveredDateTime;
    private String netTotal;
}
