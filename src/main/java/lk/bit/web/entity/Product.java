package lk.bit.web.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "item")
public class Product implements SuperEntity {

    @Id
    @Column(name = "id", length = 50, nullable = false)
    private String productId;
    @Column(name = "name", length = 50, nullable = false)
    private String productName;
    @Column(name = "description", nullable = false)
    private String productDescription;
    @Column(name = "quantity_per_unit", columnDefinition = "INT", nullable = false)
    private int quantityPerUnit;
    @Column(name = "quantity_buying_price", columnDefinition = "DECIMAL(10,2)", nullable = false)
    private BigDecimal quantityBuyingPrice;
    @Column(name = "quantity_selling_price", columnDefinition = "DECIMAL(10,2)", nullable = false)
    private BigDecimal quantitySellingPrice;
    @Column(name = "weight", length = 50, nullable = false)
    private String weight;
    @Column(name = "discount_per_unit", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal discountPerUnit;
    @Column(name = "current_quantity", columnDefinition = "INT", nullable = false)
    private int currentQuantity;
    @Lob
    @Column(name = "image_1", columnDefinition = "MEDIUMBLOB", nullable = false)
    private byte[] imageOne;
    @Lob
    @Column(name = "image_2", columnDefinition = "MEDIUMBLOB")
    private byte[] imageTwo;
    @Lob
    @Column(name = "image_3", columnDefinition = "MEDIUMBLOB")
    private byte[] imageThree;
    @Column(name = "status", length = 30, nullable = false)
    private String status;


}
