package lk.bit.web.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ComplainSolutionDTO {

    private String id;
    private String msgDescription;
    private String createdDateTime;
    private String createdBy;
    private String complaintId;
    private int isDeleted;
    private int status;
}
