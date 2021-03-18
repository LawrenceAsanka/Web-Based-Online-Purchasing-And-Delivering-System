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
@Table(name = "assigned_return")
public class AssignReturn implements SuperEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "assigned_date_time", nullable = false)
    private LocalDateTime assignedDateTime = LocalDateTime.now();

    @OneToOne
    @JoinColumn(name = "return_id", referencedColumnName = "id", nullable = false)
    private Return returnId;

    @ManyToOne
    @JoinColumn(name = "assignee_id", referencedColumnName = "id", nullable = false)
    private SystemUser assigneeId;

    public AssignReturn(Return returnId, SystemUser assigneeId) {
        this.returnId = returnId;
        this.assigneeId = assigneeId;
    }
}
