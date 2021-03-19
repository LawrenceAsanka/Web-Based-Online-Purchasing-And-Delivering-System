package lk.bit.web.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "complete_return")
public class CompleteReturn {

    @Id
    @Column(length = 30, nullable = false)
    private String id;

    @Column(name = "returned_date_time", nullable = false)
    private LocalDateTime deliveredDateTime = LocalDateTime.now();

    @OneToOne
    @JoinColumn(name = "assign_return_id", referencedColumnName = "id", nullable = false)
    private AssignReturn assignReturnId;

    public CompleteReturn(String id, AssignReturn assignReturnId) {
        this.id = id;
        this.assignReturnId = assignReturnId;
    }
}
