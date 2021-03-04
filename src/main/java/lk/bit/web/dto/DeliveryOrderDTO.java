package lk.bit.web.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DeliveryOrderDTO {

    private String customerId;
    private String customerFullName;
    private String customerContact;
    private String orderId;
    private String shopName;
    private String deliveryAddress;
    private String deliveryContact;
    private String paymentMethod;
    private String totalAmount;
}
