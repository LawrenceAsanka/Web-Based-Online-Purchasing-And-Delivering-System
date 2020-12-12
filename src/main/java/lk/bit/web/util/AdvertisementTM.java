package lk.bit.web.util;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AdvertisementTM {

    private int adsId;
    private String adsName;
    private String adsImage;
    private int dateCount;
    private String status;
}
