package lk.bit.web.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CreditorDTO {

    private String id;
    private String nicFrontImage;
    private String nicBackImage;
    private String creditDate;
    private String totalCreditAmount;
    private String lastDateToSettle;
    private int isEmailSent;
    private int isSettle;
    private String customerId;
}
