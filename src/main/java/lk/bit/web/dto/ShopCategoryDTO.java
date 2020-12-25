package lk.bit.web.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ShopCategoryDTO {

    private int categoryId;
    private String categoryName;
    private String status;
}
