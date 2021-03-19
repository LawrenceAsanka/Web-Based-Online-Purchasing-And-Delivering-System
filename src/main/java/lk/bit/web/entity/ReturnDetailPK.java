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
public class ReturnDetailPK implements Serializable {

    private String returnId;
    private String productId;

    public ReturnDetailPK(String returnId) {
        this.returnId = returnId;
    }
}
