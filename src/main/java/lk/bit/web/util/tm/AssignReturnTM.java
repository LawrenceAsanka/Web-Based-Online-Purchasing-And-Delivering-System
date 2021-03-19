package lk.bit.web.util.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AssignReturnTM {

    private String returnId;
    private String orderId;
    private String createdDateTime;
    private String customerId;
    private String customerName;
    private String assignTo;
    private String assignedDateTime;
}
