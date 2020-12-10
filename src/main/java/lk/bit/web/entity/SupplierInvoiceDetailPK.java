package lk.bit.web.entity;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SupplierInvoiceDetailPK implements Serializable {

    private String invoiceNumber;
    private String productId;

    public SupplierInvoiceDetailPK(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }
}
