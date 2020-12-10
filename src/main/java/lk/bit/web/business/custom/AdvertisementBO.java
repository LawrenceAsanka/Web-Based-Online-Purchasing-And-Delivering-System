package lk.bit.web.business.custom;

import lk.bit.web.business.SuperBO;
import org.springframework.web.multipart.MultipartFile;

public interface AdvertisementBO extends SuperBO {

    void saveAdvertisement(MultipartFile image,String advertisementName);
}
