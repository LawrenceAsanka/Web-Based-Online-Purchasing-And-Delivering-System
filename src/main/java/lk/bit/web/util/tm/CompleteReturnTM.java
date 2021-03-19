package lk.bit.web.util.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CompleteReturnTM {

    private String returnId;
    private String assignedDateTime;
    private String completedDateTime;
    private String shop;
    private String address;
}
