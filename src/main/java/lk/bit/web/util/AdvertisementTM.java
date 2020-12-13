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
    private long dateCount;
    private String createdDate;
    private String status;
}
