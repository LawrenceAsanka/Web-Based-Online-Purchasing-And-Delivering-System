package lk.bit.web.dto;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SubCategoryDTO {

    private int subCategoryId;
    private String subCategoryName;
    private String categoryId;
}
