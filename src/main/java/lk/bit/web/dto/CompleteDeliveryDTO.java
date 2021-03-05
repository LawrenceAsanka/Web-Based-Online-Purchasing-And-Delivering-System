package lk.bit.web.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CompleteDeliveryDTO {

    private String deliveryId;
    private String deliveredDateTime;
    private String orderId;
    private String deliverBy;
}
