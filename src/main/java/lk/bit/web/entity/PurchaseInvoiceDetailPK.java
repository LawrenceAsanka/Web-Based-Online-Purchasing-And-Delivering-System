package lk.bit.web.entity;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class PurchaseInvoiceDetailPK implements Serializable {

    private String purchaseInvoiceId;
    private String productId;

    public PurchaseInvoiceDetailPK(String purchaseInvoiceId) {
        this.purchaseInvoiceId = purchaseInvoiceId;
    }
}
