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

    @Column(name = "product_name", length = 50, nullable = false)
    private String productName;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String productDescription;

    @Column(name = "quantity_per_unit", columnDefinition = "INT", nullable = false)
    private int quantityPerUnit;

    @Column(name = "quantity_buying_price", columnDefinition = "DECIMAL(10,2)", nullable = false)
    private BigDecimal quantityBuyingPrice;

    @Column(name = "quantity_selling_price", columnDefinition = "DECIMAL(10,2)", nullable = false)
    private BigDecimal quantitySellingPrice;

    @Column(length = 50, nullable = false)
    private String weight;

    @Column(name = "discount_per_unit", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal discountPerUnit;

    @Column(name = "current_quantity", columnDefinition = "INT", nullable = false)
    private int currentQuantity;

    @Column(name = "image_1",length = 100,nullable = false)
    private String imageOne;

    @Column(name = "image_2",length = 100)
    private String imageTwo;

    @Column(name = "image_3", length = 100)
    private String imageThree;

    @Column(name = "offer_enabled",columnDefinition = "TINYINT DEFAULT 0", length = 1, nullable = false)
    private int offerStatus;

    @Column(length = 30, nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "subcategory_id",referencedColumnName = "id",nullable = false)
    private ProductSubCategory subCategory;

    @ManyToOne
    @JoinColumn(name = "category_id",referencedColumnName = "id",nullable = false)
    private ProductCategory category;

    public Product(String productId, String productName, String productDescription, int quantityPerUnit,
                   BigDecimal quantityBuyingPrice, BigDecimal quantitySellingPrice, String weight,
                   BigDecimal discountPerUnit, int currentQuantity, String imageOne, String imageTwo,
                   String imageThree, String status,int subCategoryId,String categoryId) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.quantityPerUnit = quantityPerUnit;
        this.quantityBuyingPrice = quantityBuyingPrice;
        this.quantitySellingPrice = quantitySellingPrice;
        this.weight = weight;
        this.discountPerUnit = discountPerUnit;
        this.currentQuantity = currentQuantity;
        this.imageOne = imageOne;
        this.imageTwo = imageTwo;
        this.imageThree = imageThree;
        this.status = status;
        this.subCategory = new ProductSubCategory(subCategoryId);
        this.category = new ProductCategory(categoryId);
    }
}
