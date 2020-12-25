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

    @Column(length = 20, nullable = false)
    private String status;

    public ShopCategory(String categoryName, String status) {
        this.categoryName = categoryName;
        this.status = status;
    }
}
