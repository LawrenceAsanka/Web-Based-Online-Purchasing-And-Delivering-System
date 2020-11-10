package lk.bit.web.dto;

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
