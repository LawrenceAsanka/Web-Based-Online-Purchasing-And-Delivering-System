package lk.bit.web.util.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CompleteCreditCollectionTM {

    private String creditId;
    private String creditDate;
    private String creditor;
    private String collectBy;
    private String collectedDate;
    private String creditAmount;
}
