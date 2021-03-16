package lk.bit.web.dto;

import lombok.*;

import java.util.List;

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
    private List<ReturnDetailDTO> returnDetailDTOList;
}
