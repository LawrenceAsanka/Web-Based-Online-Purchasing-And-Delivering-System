package lk.bit.web.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "item_sub_category")
public class ProductSubCategory implements SuperEntity {

    @Id
    @Column(name = "id")
    private String subCategoryId;
    @Column(name = "name")
    private String subCategoryName;
    private String status;
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private ProductCategory productCategory;

    public ProductSubCategory(String subCategoryId, String subCategoryName, String status, String productCategoryId) {
        this.subCategoryId = subCategoryId;
        this.subCategoryName = subCategoryName;
        this.status = status;
        this.productCategory = new ProductCategory(productCategoryId);
    }
}
