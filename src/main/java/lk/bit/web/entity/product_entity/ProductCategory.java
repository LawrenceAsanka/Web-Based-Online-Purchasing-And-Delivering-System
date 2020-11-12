package lk.bit.web.entity.product_entity;

import lk.bit.web.entity.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "item_category")
public class ProductCategory implements SuperEntity {

    @Id
    @Column(name = "id", length = 50, nullable = false)
    private String categoryId;
    @Column(name = "name", length = 50, nullable = false)
    private String categoryName;
    @Column(name = "status", length = 50, nullable = false)
    private String status;

    public ProductCategory(String categoryId){
        this.categoryId = categoryId;
    }
}
