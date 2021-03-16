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
@Table(name = "`return`")
public class Return implements SuperEntity {

    @Id
    @Column(length = 30)
    private String id;

    @Column(name = "created_date_time", nullable = false)
    private LocalDateTime createdDateTime = LocalDateTime.now();

    @Column(nullable = false, columnDefinition = "tinyint(1) DEFAULT 0")
    private int status = 0;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id", nullable = false)
    private OrderInvoice orderId;
}
