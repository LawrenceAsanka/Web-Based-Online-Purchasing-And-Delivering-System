package lk.bit.web.dto.product_dto;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SubCategoryDTO {

    private String subCategoryId;
    private String subCategoryName;
    private String status;
    private String categoryId;
}
