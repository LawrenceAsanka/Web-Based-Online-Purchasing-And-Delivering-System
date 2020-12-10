package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.AdvertisementBO;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Component
public class AdvertisementBOImpl implements AdvertisementBO {

    private static File file;
    private String uploadDir;

    @Override
    public void saveAdvertisement(MultipartFile image, String advertisementName) {

    }
}
