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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "delivery_date_time", nullable = false)
    private LocalDateTime deliveryDateTime = LocalDateTime.now();

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
    private AssignOrderInvoiceDetail orderId;

    @ManyToOne
    @JoinColumn(name = "deliver_by", referencedColumnName = "id", nullable = false)
    private SystemUser deliveryBy;

    public CompleteDelivery(int id, AssignOrderInvoiceDetail orderId, SystemUser deliveryBy) {
        this.id = id;
        this.orderId = orderId;
        this.deliveryBy = deliveryBy;
    }
}
