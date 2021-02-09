package lk.bit.web.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "order_invoice")
public class OrderInvoice implements SuperEntity{

    @Id
    @Column(name = "order_id", length = 20)
    private String orderId;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    private CustomerUser customerUser;

    @ManyToOne
    @JoinColumn(name = "shop_id", referencedColumnName = "id", nullable = false)
    private Shop shop;

    @Column(name="created_date_time", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private Date createdDateAndTime;

    @Column(name = "net_total", nullable = false)
    private BigDecimal netTotal;

    @Column(nullable = false, columnDefinition = "tinyint(1) DEFAULT 0")
    private int status;


}
