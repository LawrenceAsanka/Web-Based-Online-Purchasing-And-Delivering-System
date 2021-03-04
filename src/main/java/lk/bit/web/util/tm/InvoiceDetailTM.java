package lk.bit.web.util.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class InvoiceDetailTM {

    private String invoiceNumber;
    private String dateTime;
    private String userId;
    private String userName;
    private String netAmount;
}
