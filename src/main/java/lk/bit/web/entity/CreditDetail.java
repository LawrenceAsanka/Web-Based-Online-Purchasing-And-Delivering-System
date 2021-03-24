package lk.bit.web.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "creditor_detail")
public class CreditDetail implements SuperEntity {

    @Id
    @Column(length = 30)
    private String id;

    @Column(name = "nic_front_image", length = 100, nullable = false)
    private String nicFrontImage;

    @Column(name = "nic_back_image", length = 100, nullable = false)
    private String nicBackImage;

    @Column(name = "credit_date", nullable = false)
    private LocalDateTime creditDate = LocalDateTime.now();

    @Column(name = "total_credit_amount", nullable = false)
    private BigDecimal totalCreditAmount;

    @Column(name = "last_date_to_settle", nullable = false)
    private LocalDateTime lastDateToSettle;

    @Column(name = "is_email_sent", columnDefinition = "TINYINT DEFAULT 0")
    private int isEmailSent;

    @Column(name = "is_settle", columnDefinition = "TINYINT DEFAULT 0")
    private int isSettle;

    @Column(name = "is_assigned", columnDefinition = "TINYINT DEFAULT 0")
    private int isAssigned;

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id" , nullable = false)
    private OrderInvoice orderInvoice;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    private CustomerUser customerUser;

    public CreditDetail(String id, String nicFrontImage, String nicBackImage, BigDecimal totalCreditAmount,
                        LocalDateTime lastDateToSettle, OrderInvoice orderInvoice, CustomerUser customerUser) {
        this.id = id;
        this.nicFrontImage = nicFrontImage;
        this.nicBackImage = nicBackImage;
        this.totalCreditAmount = totalCreditAmount;
        this.lastDateToSettle = lastDateToSettle;
        this.orderInvoice = orderInvoice;
        this.customerUser = customerUser;
    }
}
