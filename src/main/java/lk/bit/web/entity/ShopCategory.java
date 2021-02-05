package lk.bit.web.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "shop_category")
public class ShopCategory implements SuperEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", length = 20)
    private int categoryId;

    @Column(name = "category_name", length = 50, nullable = false)
    private String categoryName;

    @Column(nullable = false, columnDefinition = "tinyint(1)")
    private int status = 1;

    public ShopCategory(String categoryName) {
        this.categoryName = categoryName;
    }
}
