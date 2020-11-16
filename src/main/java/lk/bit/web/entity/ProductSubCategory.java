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
    @Column(name = "id",length = 50,nullable = false)
    private String subCategoryId;
    @Column(name = "name", length = 50, nullable = false)
    private String subCategoryName;
    @Column(name = "status", length = 50, nullable = false)
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

    public ProductSubCategory(String subCategoryId){
        this.subCategoryId = subCategoryId;
    }
}
