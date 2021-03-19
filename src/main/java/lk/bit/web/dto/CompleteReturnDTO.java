package lk.bit.web.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CompleteReturnDTO {

    private String id;
    private String returnedDateTime;
    private String assignReturnId;
}
