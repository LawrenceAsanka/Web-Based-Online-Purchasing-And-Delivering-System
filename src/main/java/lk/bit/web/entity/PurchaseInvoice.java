package lk.bit.web.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "purchase_invoice")
public class PurchaseInvoice {

    @Id
    @Column(length = 30)
    private String id;

    @Column(name = "purchase_date_time",nullable = false)
    private LocalDateTime purchaseDateTime = LocalDateTime.now();

    public PurchaseInvoice(String id) {
        this.id = id;
    }
}
