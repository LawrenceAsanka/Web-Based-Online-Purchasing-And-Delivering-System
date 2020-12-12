package lk.bit.web.business.custom.impl;

import lk.bit.web.business.custom.AdvertisementBO;
import lk.bit.web.entity.Advertisement;
import lk.bit.web.repository.AdvertisementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@Component
public class AdvertisementBOImpl implements AdvertisementBO {

    @Autowired
    private Environment env;
    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Override
    public void saveAdvertisement(MultipartFile image, String advertisementName) {

        // check whether folder is exist or not
        String uploadDir = env.getProperty("static.path") + "ads/";
        File file = new File(uploadDir);
        if (!file.exists()) {
            file.mkdir();
        }

        // save data into database
        advertisementRepository.save(new Advertisement(
                advertisementName, image.getOriginalFilename(), new Date(), "AVAILABLE"
        ));

        // save image in locally
        try {
            image.transferTo(new File(file.getAbsolutePath() + "/" + image.getOriginalFilename()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
