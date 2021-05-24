package lk.bit.web.util.tm;

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
    private String image1;
    private String image2;
    private String image3;
    private String status;

}
