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
@Table(name = "complaint")
public class Complaint {

    @Id
    @Column(length = 20)
    private String id;

    @Column(name = "msg_subject", length = 100,nullable = false)
    private String msgSubject;

    @Column(name = "msg_description", nullable = false)
    private String msgDescription;

    @Column(name = "created_date_time", nullable = false)
    private LocalDateTime createdDateTime = LocalDateTime.now();

    @Column(name = "is_deleted_by_customer",columnDefinition = "tinyint(1)",nullable = false)
    private int isDeleteByCustomer = 0;

    @Column(name = "is_deleted_by_admin", columnDefinition = "tinyint(1)" , nullable = false)
    private int isDeletedByAdmin = 0;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id", nullable = false)
    private CustomerUser customer;

    public Complaint(String id, String msgSubject, String msgDescription, CustomerUser customer) {
        this.id = id;
        this.msgSubject = msgSubject;
        this.msgDescription = msgDescription;
        this.customer = customer;
    }
}
