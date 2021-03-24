package lk.bit.web.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CreditDetailDTO {

    private String id;
    private String nicFrontImage;
    private String nicBackImage;
    private String creditDate;
    private String totalCreditAmount;
    private String lastDateToSettle;
    private int isEmailSent;
    private int isSettle;
    private int isAssigned;
    private String customerId;
}
