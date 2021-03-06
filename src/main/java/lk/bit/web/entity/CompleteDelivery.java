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
@Table(name = "complete_delivery")
public class CompleteDelivery {

    @Id
    @Column(length = 30, nullable = false)
    private String id;

    @Column(name = "delivered_date_time", nullable = false)
    private LocalDateTime deliveredDateTime = LocalDateTime.now();

    @OneToOne
    @JoinColumn(name = "assign_invoice_id", referencedColumnName = "id", nullable = false)
    private AssignOrderInvoiceDetail assignInvoiceId;

    public CompleteDelivery(String id, AssignOrderInvoiceDetail assignInvoiceId) {
        this.id = id;
        this.assignInvoiceId = assignInvoiceId;
    }
}
