package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.AdvertisementBO;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class AdvertisementBOImpl implements AdvertisementBO {

    @Override
    public void saveAdvertisement(MultipartFile image, String advertisementName) {

    }
}
