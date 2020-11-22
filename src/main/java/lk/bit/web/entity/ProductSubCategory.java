package lk.bit.web.entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name = "item_sub_category")
public class ProductSubCategory implements SuperEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",length = 50,columnDefinition = "INT",nullable = false)
    private String subCategoryId;

    @Column(name = "sub_category_name",nullable = false,length = 50)
    private String subCategoryName;

    @ManyToOne
    @JoinColumn(name = "category_id",referencedColumnName = "id",nullable = false)
    private ProductCategory category;
}
