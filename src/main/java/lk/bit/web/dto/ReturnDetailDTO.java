package lk.bit.web.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ReturnDetailDTO {

    private String productId;
    private int returnQty;
    private String reasonToReturn;
}
