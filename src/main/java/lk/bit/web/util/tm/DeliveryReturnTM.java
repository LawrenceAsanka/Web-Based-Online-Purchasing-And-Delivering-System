package lk.bit.web.util.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DeliveryReturnTM {

    private String returnId;
    private String orderId;
    private String customerName;
    private String returnAddress;
    private String assignedDateTime;
}
