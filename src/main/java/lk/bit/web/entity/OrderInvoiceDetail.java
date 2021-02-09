package lk.bit.web.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "order_invoice_detail")
public class OrderInvoiceDetail implements SuperEntity {

    @EmbeddedId
    @AttributeOverride(name = "orderInvoiceId", column = @Column(name = "order_invoice_id",length = 20))
    @AttributeOverride(name = "productId", column = @Column(name = "product_id", length = 20))
    private OrderInvoiceDetailPK orderInvoiceDetailPK;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(nullable = false)
    private BigDecimal discount;

    @ManyToOne
    @JoinColumn(name = "order_invoice_id",referencedColumnName = "order_id",insertable = false,updatable = false)
    private OrderInvoice orderInvoice;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Product product;

    public OrderInvoiceDetail(String orderInvoiceId, String productId, BigDecimal total,
                              BigDecimal discount, OrderInvoice orderInvoice, Product product) {
        this.orderInvoiceDetailPK = new OrderInvoiceDetailPK(orderInvoiceId, productId);
        this.total = total;
        this.discount = discount;
        this.orderInvoice = orderInvoice;
        this.product = product;
    }
}
