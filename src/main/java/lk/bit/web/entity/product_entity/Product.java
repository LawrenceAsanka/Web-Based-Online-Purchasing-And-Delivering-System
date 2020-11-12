package lk.bit.web.entity.product_entity;

import lk.bit.web.entity.SuperEntity;
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
    @Column(name = "category", length = 100, nullable = false)
    private String productCategory;
    @Column(name = "quantity_per_unit", columnDefinition = "INT", nullable = false)
    private int quantityPerUnit;
    @Column(name = "quantity_buying_price", columnDefinition = "DECIMAL(10,2)", nullable = false)
    private BigDecimal quantityBuyingPrice;
    @Column(name = "quantity_selling_price", columnDefinition = "DECIMAL(10,2)", nullable = false)
    private BigDecimal quantitySellingPrice;
    @Column(name = "weight", length = 50, nullable = false)
    private String weight;
    @Column(name = "discount_per_unit", columnDefinition = "DECIMAL(5,2) DEFAULT 0.00", nullable = false)
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
    @ManyToOne
    @JoinColumn(name = "sub_category_id",
            referencedColumnName = "id", nullable = false)
    private ProductSubCategory subCategory;

    public Product(String productId, String productName,
                   String productDescription, String productCategory,
                   int quantityPerUnit, BigDecimal quantityBuyingPrice,
                   BigDecimal quantitySellingPrice, String weight, BigDecimal discountPerUnit,
                   int currentQuantity, byte[] imageOne, byte[] imageTwo, byte[] imageThree,
                   String subCategoryId) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productCategory = productCategory;
        this.quantityPerUnit = quantityPerUnit;
        this.quantityBuyingPrice = quantityBuyingPrice;
        this.quantitySellingPrice = quantitySellingPrice;
        this.weight = weight;
        this.discountPerUnit = discountPerUnit;
        this.currentQuantity = currentQuantity;
        this.imageOne = imageOne;
        this.imageTwo = imageTwo;
        this.imageThree = imageThree;
        this.subCategory = new ProductSubCategory(subCategoryId);
    }
}
