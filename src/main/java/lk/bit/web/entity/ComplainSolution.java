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
@Table(name = "complain_solution")
public class ComplainSolution {

    @Id
    @Column(length = 20)
    private String id;

    @Column(name = "msg_description", columnDefinition = "TEXT", nullable = false)
    private String msgDescription;

    @Column(name = "created_date_time", nullable = false)
    private LocalDateTime createdDateTime = LocalDateTime.now();

    @Column(name = "is_deleted", columnDefinition = "TINYINT(1)", nullable = false)
    private int isDeleted = 0;

    @Column(columnDefinition = "TINYINT(1)",nullable = false)
    private int status = 0;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id", nullable = false)
    private SystemUser systemUser;

    @OneToOne
    @JoinColumn(name = "complain_id", referencedColumnName = "id", nullable = false)
    private Complain complain;

    public ComplainSolution(String id, String msgDescription, SystemUser systemUser, Complain complain) {
        this.id = id;
        this.msgDescription = msgDescription;
        this.systemUser = systemUser;
        this.complain = complain;
    }
}
