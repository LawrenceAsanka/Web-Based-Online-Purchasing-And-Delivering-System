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
@Table(name = "assign_order_invoice_detail")
public class AssignOrderInvoiceDetail implements SuperEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "assigned_date_time", nullable = false)
    private LocalDateTime assignedDateTime = LocalDateTime.now();

    @OneToOne
    @JoinColumn(name = "order_invoice_id", referencedColumnName = "order_id", nullable = false)
    private OrderInvoice orderInvoiceId;

    @ManyToOne
    @JoinColumn(name = "assignee_id", referencedColumnName = "id", nullable = false)
    private SystemUser assigneeId;

    public AssignOrderInvoiceDetail(OrderInvoice orderInvoiceId, SystemUser assigneeId) {
        this.orderInvoiceId = orderInvoiceId;
        this.assigneeId = assigneeId;
    }
}
