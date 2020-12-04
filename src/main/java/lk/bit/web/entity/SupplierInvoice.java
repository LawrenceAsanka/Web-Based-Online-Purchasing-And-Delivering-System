package lk.bit.web.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date_time")
    private Date dateAndTime;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "user_id", referencedColumnName = "auth_user_id", nullable = false)
    private User user;


}
