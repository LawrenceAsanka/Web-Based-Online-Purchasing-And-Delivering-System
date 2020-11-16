package lk.bit.web.dto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ProductDTO {

    private String productId;
    private String productName;
    private String productDescription;
    private int quantityPerUnit;
    private BigDecimal quantityBuyingPrice;
    private BigDecimal quantitySellingPrice;
    private String weight;
    private BigDecimal discountPerUnit;
    private int currentQuantity;
    private String imageOne;
    private String imageTwo;
    private String imageThree;
    private String status;
    private String productSubCategoryId;

}
