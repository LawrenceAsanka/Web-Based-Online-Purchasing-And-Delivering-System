package lk.bit.web.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "supplier_invoice_detail")
public class SupplierInvoiceDetail implements SuperEntity {

    @EmbeddedId
    @AttributeOverride(name = "invoiceNumber",column = @Column(name = "supplier_invoice_id"))
    @AttributeOverride(name = "productId",column = @Column(name = "product_id"))
    private SupplierInvoiceDetailPK supplierInvoiceDetailPK;

    @Column(length = 20)
    private int qty;

    @Column(name = "qty_price")
    private BigDecimal qtyPrice;

    @Column
    private BigDecimal discount;

    @ManyToOne
    @JoinColumn(name = "supplier_invoice_id",referencedColumnName = "invoice_id",insertable = false,updatable = false)
    private SupplierInvoice supplierInvoice;

    @ManyToOne
    @JoinColumn(name = "product_id",referencedColumnName = "id",insertable = false,updatable = false)
    private Product product;
}
