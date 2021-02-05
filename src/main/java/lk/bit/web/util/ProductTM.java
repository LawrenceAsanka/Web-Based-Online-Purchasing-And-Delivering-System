package lk.bit.web.util;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ProductTM {

    private String productId;
    private String productName;
    private String productCategory;
    private String sellingPrice;
    private String buyingPrice;
    private String quantity;
    private String discountPerUnit;
    private String offerStatus;
    private String status;

}
