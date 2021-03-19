package lk.bit.web.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class AssignReturnDTO {

    private int id;
    private String dateTime;
    private String returnId;
    private String assigneeId;
}
