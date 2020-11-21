package lk.bit.web.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "item_category")
public class ProductCategory implements SuperEntity {

    @Id
    @Column(name = "id", length = 50, nullable = false)
    private String categoryId;

    @Column(name = "name", length = 50, nullable = false, unique = true)
    private String categoryName;

    @Column(name = "status", length = 50, nullable = false)
    private String status;

}
