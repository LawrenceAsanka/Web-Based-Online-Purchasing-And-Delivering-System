package lk.bit.web.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @Column(name = "net_total", nullable = false)
    private BigDecimal netTotal;

    @Column(name = "payment_method",length = 30, nullable = false)
    private String paymentMethod;

    @Column(name="created_date_time",nullable = false)
    private LocalDateTime createdDateAndTime = LocalDateTime.now();

    @Column(name="deadline_date_time", nullable = false)
    private LocalDateTime deadlineDateAndTime;

    @Column(nullable = false, columnDefinition = "tinyint(1) DEFAULT 0")
    private int status = 0;

    public OrderInvoice(String orderId, CustomerUser customerUser, Shop shop, BigDecimal netTotal ,
                        String paymentMethod, LocalDateTime deadlineDateAndTime) {
        this.orderId = orderId;
        this.customerUser = customerUser;
        this.shop = shop;
        this.netTotal = netTotal;
        this.paymentMethod = paymentMethod;
        this.deadlineDateAndTime = deadlineDateAndTime;
    }
}
