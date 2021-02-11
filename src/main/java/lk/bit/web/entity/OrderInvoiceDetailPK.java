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
public class OrderInvoiceDetailPK implements Serializable {

    private String orderInvoiceId;
    private String productId;

    public OrderInvoiceDetailPK(String orderInvoiceId) {
        this.orderInvoiceId = orderInvoiceId;
    }
}
