package lk.bit.web.dto.product_dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ProductCategoryDTO {

    private String categoryId;
    private String categoryName;
    private String status;
}
