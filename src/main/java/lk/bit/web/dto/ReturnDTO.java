package lk.bit.web.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ReturnDTO {

    private String returnId;
    private String createdDateTime;
    private String orderId;
    private int status;
}
