package lk.bit.web.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "supplier_invoice")
public class SupplierInvoice implements SuperEntity {

    @Id
    @Column(name = "invoice_id",length = 50)
    private String invoiceNumber;

    @Column(name = "created_date_time",nullable = false)
    private String dateAndTime;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "created_by", referencedColumnName = "id", nullable = false)
    private SystemUser systemUser;

}
