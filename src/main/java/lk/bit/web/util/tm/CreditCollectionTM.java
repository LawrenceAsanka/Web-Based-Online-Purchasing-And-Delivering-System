package lk.bit.web.util.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CreditCollectionTM {

    private String creditId;
    private String customer;
    private String shop;
    private String address;
    private String creditDateTime;
    private String creditAmount;
}
