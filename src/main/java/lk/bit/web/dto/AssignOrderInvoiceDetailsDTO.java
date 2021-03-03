package lk.bit.web.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class AssignOrderInvoiceDetailsDTO {

    private int id;
    private String dateTime;
    private String orderId;
    private String assigneeId;
}
