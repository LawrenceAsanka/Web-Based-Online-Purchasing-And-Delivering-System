package lk.bit.web.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "return_detail")
public class ReturnDetail implements SuperEntity{

    @EmbeddedId
    @AttributeOverride(name = "returnId", column = @Column(name = "return_id",length = 20))
    @AttributeOverride(name = "productId", column = @Column(name = "product_id", length = 20))
    private ReturnDetailPK returnDetailPK;

    @Column(name= "return_qty", nullable = false, length = 10)
    private int returnQty;

    @Column(name = "reason_to_return", nullable = false, length = 50)
    private String ReasonToReturn;

    @ManyToOne
    @JoinColumn(name = "return_id",referencedColumnName = "id",insertable = false,updatable = false)
    private Return returnId;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Product product;

    public ReturnDetail(String returnId, String productId, int returnQty, String reasonToReturn) {
        this.returnDetailPK = new ReturnDetailPK(returnId, productId);
        this.returnQty = returnQty;
        ReasonToReturn = reasonToReturn;
    }
}
