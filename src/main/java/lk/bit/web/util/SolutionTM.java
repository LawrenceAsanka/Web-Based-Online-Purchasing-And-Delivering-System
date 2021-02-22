package lk.bit.web.util;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SolutionTM {

    private String complainId;
    private String complaintSubject;
    private String complaintDesc;
    private String complainCreatedDate;
    private String solutionCreatedDate;
    private String solutionDesc;
    private int solutionStatus;
}
