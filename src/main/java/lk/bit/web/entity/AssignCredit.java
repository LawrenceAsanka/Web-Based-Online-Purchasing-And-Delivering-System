package lk.bit.web.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "assigned_credit")
public class AssignCredit implements SuperEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "assigned_date_time", nullable = false)
    private LocalDateTime assignedDateTime = LocalDateTime.now();

    @OneToOne
    @JoinColumn(name = "credit_id", referencedColumnName = "id", nullable = false)
    private CreditDetail creditDetail;

    @ManyToOne
    @JoinColumn(name = "assignee_id", referencedColumnName = "id", nullable = false)
    private SystemUser assigneeId;

    public AssignCredit(CreditDetail creditDetail, SystemUser assigneeId) {
        this.creditDetail = creditDetail;
        this.assigneeId = assigneeId;
    }
}
