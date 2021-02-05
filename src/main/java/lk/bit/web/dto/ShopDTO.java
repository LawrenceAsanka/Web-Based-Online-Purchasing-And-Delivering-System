package lk.bit.web.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ShopDTO {

    private String id;
    private String name;
    private String contact;
    private int category;
    private String address1;
    private String address2;
    private String district;
    private String city;

}
