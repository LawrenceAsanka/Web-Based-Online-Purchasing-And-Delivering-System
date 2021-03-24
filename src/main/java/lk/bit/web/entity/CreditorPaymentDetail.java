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
@Table(name = "creditor_payment_detail")
public class CreditorPaymentDetail implements SuperEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "paid_date_time", nullable = false)
    private LocalDateTime paidDateTime = LocalDateTime.now();

   /* @OneToMany
    @JoinColumn(name = "paid_to", referencedColumnName =  "id", nullable = false)
    private SystemUser paidTo;
    */
    @ManyToOne
    @JoinColumn(name = "creditor_id",referencedColumnName = "id" , nullable = false)
    private Creditor creditor;

}
