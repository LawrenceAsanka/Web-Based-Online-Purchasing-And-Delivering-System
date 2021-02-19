package lk.bit.web.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ComplainDTO {

    private String id;
    private String msgSubject;
    private String msgDescription;
    private String createdDateTime;
    private int isDeletedByCustomer;
    private int isDeletedByAdmin;
    private String createdBy;
}
