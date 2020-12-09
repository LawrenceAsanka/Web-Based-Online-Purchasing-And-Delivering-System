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
    @AttributeOverride(name = "invoiceNumber",column = @Column(name = "supplier_invoice_id",length = 50))
    @AttributeOverride(name = "productId",column = @Column(name = "product_id",length = 50))
    private SupplierInvoiceDetailPK supplierInvoiceDetailPK;

    @Column(length = 20,nullable = false)
    private int qty;

    @Column(name = "qty_price",nullable = false)
    private BigDecimal qtyPrice;

    @Column(nullable = false)
    private BigDecimal discount;

    @ManyToOne
    @JoinColumn(name = "supplier_invoice_id",referencedColumnName = "invoice_id",insertable = false,updatable = false)
    private SupplierInvoice supplierInvoice;

    @ManyToOne
    @JoinColumn(name = "product_id",referencedColumnName = "id",insertable = false,updatable = false)
    private Product product;

    public SupplierInvoiceDetail(String invoiceNumber,String productCode, int qty, BigDecimal qtyPrice,
                                 BigDecimal discount) {
        this.supplierInvoiceDetailPK = new SupplierInvoiceDetailPK(invoiceNumber,productCode);
        this.qty = qty;
        this.qtyPrice = qtyPrice;
        this.discount = discount;
    }
}
