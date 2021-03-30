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
@Table(name = "purchase_invoice_detail")
public class PurchaseInvoiceDetail implements SuperEntity {

    @EmbeddedId
    @AttributeOverride(name = "purchaseInvoiceId", column = @Column(name = "purchase_invoice_id",length = 30))
    @AttributeOverride(name = "productId", column = @Column(name = "product_id", length = 20))
    private PurchaseInvoiceDetailPK purchaseInvoiceDetailPK;

    @Column(name = "purchase_quantity", nullable = false)
    private int purchaseQuantity;

    @ManyToOne
    @JoinColumn(name = "purchase_invoice_id",referencedColumnName = "id",insertable = false,updatable = false)
    private PurchaseInvoice purchaseInvoice;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Product product;

    public PurchaseInvoiceDetail(String purchaseInvoiceId, String productId, int purchaseQuantity) {
        this.purchaseInvoiceDetailPK = new PurchaseInvoiceDetailPK(purchaseInvoiceId, productId);
        this.purchaseQuantity = purchaseQuantity;
    }
}
